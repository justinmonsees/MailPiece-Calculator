/*
This class is used to interact with the database. Rather than using a DAO(future modifications will most likely including the use of Hibernate), this class
defines simple methods that make SQL calls to the database to read, write, and delete data
 */
package com.monsees.mailpiececalc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PaperDB {

    private Connection connect() {

        // SQLite connection string
        String url = "jdbc:sqlite:" + "./paper.db";

        //create the connection object
        //This has to be created outside of a try catch block
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connected Successfully to " + url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Connection Failed");
        }
        return conn;

    }

    //This function is used to query the database for papers based the type or weight
    //and returns an ArrayList of paper names
    public ArrayList getPapers(String paper_type, String paper_weight) {

        ArrayList papers = new ArrayList();
        String sql = null;

        if (paper_type.equals("ALL") && paper_weight.equals("ALL")) {
            sql = "SELECT paper_name FROM paper";
        } else if (!paper_type.equals("ALL") && paper_weight.equals("ALL")) {
            sql = "SELECT paper_name FROM paper WHERE paper_type = '" + paper_type + "'";
        } else if (paper_type.equals("ALL") && !paper_weight.equals("ALL")) {
            sql = "SELECT paper_name FROM paper WHERE weight_lb = '" + paper_weight + "'";
        } else {
            sql = "SELECT paper_name FROM paper WHERE paper_type = '" + paper_type + "' AND weight_lb = '" + paper_weight + "'";
        }

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                papers.add(rs.getString("paper_name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return (papers);
    }

    //This function is used to get the paper type for a particular paper
    public String getPaperType(String paper_name) {

        String paper_type = null;
        String sql = null;

        sql = "SELECT paper_type FROM paper WHERE paper_name = '" + paper_name + "'";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            paper_type = rs.getString("paper_type");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return (paper_type);
    }

    //This function is used to get only the unique values for a paper type
    //This will be used to filter papers by paper type
    public ArrayList getUniquePaperType() {

        ArrayList paper_types = new ArrayList();
        String sql = null;

        sql = "SELECT DISTINCT paper_type FROM paper";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                paper_types.add(rs.getString("paper_type"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return (paper_types);
    }

    //This function is used by the Paper Manager to add a new paper to the database
    public void addPaper(String paperName, String paperType, Double paperWeight, Double paperCaliper) {

        //get the last paper number in the table by sorting the rows in descending order and getting the first row
        String sql = "SELECT paper_num FROM paper ORDER BY paper_num DESC LIMIT 1";
        int last_index = 0;

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            rs.next();

            last_index = rs.getInt("paper_num");
            System.out.println("Last Index: " + last_index);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        //add the new paper to the database
        int newRowNum = last_index + 1;

        sql = "INSERT INTO paper VALUES (" + newRowNum + ",'" + paperName + "','"
                + paperType + "'," + paperWeight + "," + paperCaliper + ")";
        System.out.println(sql);
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //This function is used by the Paper Manager to add a new envelope to the paper database
    public void addEnvelope(String envName, Double envWeight, Double envCaliper) {

        //get the last paper number in the table by sorting the rows in descending order and getting the first row
        String sql = "SELECT env_num FROM envelope ORDER BY env_num DESC LIMIT 1";
        int last_index = 0;

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            rs.next();

            last_index = rs.getInt("env_num");
            System.out.println("Last Index: " + last_index);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        //add the new paper to the database
        int newRowNum = last_index + 1;

        sql = "INSERT INTO envelope VALUES (" + newRowNum + ",'" + envName + "',"
                + envWeight + "," + envCaliper + ")";
        System.out.println(sql);
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deletePaper(String paperName) {

        String sql = "DELETE FROM paper WHERE paper_name = '" + paperName + "'";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //This function is used by the Paper Manager to delete an envelope from the database
    public void deleteEnvelope(String envName) {

        String sql = "DELETE FROM envelope WHERE env_name = '" + envName + "'";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //This function is used to get the different paper weights for the papers in the database
    public ArrayList getUniquePaperWeights() {

        ArrayList paper_weights = new ArrayList();
        String sql = null;

        sql = "SELECT DISTINCT weight_lb FROM paper";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                paper_weights.add(rs.getDouble("paper_weights"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return (paper_weights);
    }

    //This is used by the Paper Manager to list the paper types for a paper that will
    //be added to the database
    public ArrayList getAllPaperTypes() {

        ArrayList paper_types = new ArrayList();
        String sql = null;

        sql = "SELECT paper_type FROM basis_weight";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                paper_types.add(rs.getString("paper_type"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return (paper_types);
    }

    //This function is used to get the paper weight in pounds for a specific paper
    public double getPaperWeightLB(String paper_name) {

        double paper_weight = 0;
        String sql = null;

        sql = "SELECT weight_lb FROM paper WHERE paper_name = '" + paper_name + "'";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                paper_weight = rs.getDouble("weight_lb");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return (paper_weight);
    }

    //This function is used to get the thickness for a specific paper
    public double getPaperCaliper(String paper_name) {

        double paper_caliper = 0;
        String sql = null;

        sql = "SELECT caliper FROM paper WHERE paper_name = '" + paper_name + "'";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                paper_caliper = rs.getDouble("caliper");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return (paper_caliper);
    }

    //This function is used to get the paper weight in GSM for a specific paper
    public double getPaperGSM(String paper_name) {

        String paper_type = this.getPaperType(paper_name);

        double paper_caliper = 0;
        String sql = null;
        double width = 0, height = 0;

        sql = "SELECT width,height FROM basis_weight WHERE paper_type = '" + paper_type + "'";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            width = rs.getDouble("width");
            height = rs.getDouble("height");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        //perform the calculations to get the GSM of a paper
        double basisSize = width * height;
        double basisWeight = this.getPaperWeightLB(paper_name);

        double gsm = (((basisWeight * 1406.5) / basisSize) * 100.0) / 100.0;
        gsm = Math.round(gsm * 100.0) / 100.0;
        return (gsm);
    }

    //This function validates if a specific paper exists in the database already
    //If it does TRUE is returned, if not it returns FALSE
    public boolean paperExists(String paper_name) {

        boolean paperExists = false;
        ArrayList papers = new ArrayList();
        String sql = "SELECT paper_name FROM paper";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                if (rs.getString("paper_name").toLowerCase().equals(paper_name.toLowerCase())) {
                    paperExists = true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return (paperExists);
    }

    //This function validates if a specific envelope exists in the database already
    //If it does TRUE is returned, if not it returns FALSE
    public boolean envelopeExists(String env_name) {

        boolean envelopeExists = false;
        ArrayList envelopes = new ArrayList();
        String sql = "SELECT env_name FROM envelope";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                if (rs.getString("env_name").toLowerCase().equals(env_name.toLowerCase())) {
                    envelopeExists = true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return (envelopeExists);
    }

    //This function returns an ArrayList of all the envelope names in the database
    public ArrayList getEnvelopes() {

        ArrayList envelopes = new ArrayList();
        String sql = "SELECT env_name FROM envelope";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                envelopes.add(rs.getString("env_name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return (envelopes);
    }

    //This function returns the weight in ounces for a single envelope
    public double getEnvelopeWeight(String env_name) {

        double envWeight = 0;
        String sql = "SELECT weight_oz FROM envelope WHERE env_name = '" + env_name + "'";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            envWeight = rs.getDouble("weight_oz");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return (envWeight);
    }

    //This function returns the thickness for a single envelope
    public double getEnvelopeCaliper(String env_name) {

        double envCaliper = 0;
        String sql = "SELECT caliper FROM envelope WHERE env_name = '" + env_name + "'";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            envCaliper = rs.getDouble("caliper");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return (envCaliper);
    }
}
