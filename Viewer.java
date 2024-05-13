package Fetching_Data;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Viewer extends JFrame {
    private JFormattedTextField startDateField;
    private JFormattedTextField endDateField;
    private JButton fetchButton;
    private JTextArea textArea;
    private JTable table;

    public Viewer() {
        setTitle("Data Viewer");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create formatted text fields for dates
        startDateField = new JFormattedTextField(createFormatter("####-##-##"));
        endDateField = new JFormattedTextField(createFormatter("####-##-##"));

        // Create fetch button
        fetchButton = new JButton("Fetch Data");

        // Create text area to display data
        textArea = new JTextArea(20, 60);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Add components to the frame
        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Start Date:"));
        formPanel.add(startDateField);
        formPanel.add(new JLabel("End Date:"));
        formPanel.add(endDateField);
        formPanel.add(fetchButton);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Fetch button action
        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchButtonClicked();
            }
        });
        setVisible(true);
    }

    private void fetchButtonClicked() {
        // Retrieve entered start and end dates
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();

        // Perform actions with entered dates (e.g., fetch data from API)
//        System.out.println("Start Date: " + startDate);
//        System.out.println("End Date: " + endDate);

        // Fetch the data from the URL
        new Thread(() -> {
            try {
                URL url = new URL("https://piptrade.org/webtest/testingdata.json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Parse JSON data
                    JSONArray jsonArray = new JSONArray(response.toString());

                    // Display data in JTextArea
                    displayDataInTextArea(jsonArray);

                    // Display data in JTable
                    displayDataInTable(jsonArray);
                } else {
                    System.out.println("Failed to fetch the data");
                }
            } catch (Exception ex) {
                System.out.println("Error fetching data: " + ex.getMessage());
            }
        }).start();
    }

    private void displayDataInTextArea(JSONArray jsonArray) throws JSONException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            sb.append(jsonObject.toString()).append("\n");
        }
        textArea.setText(sb.toString());
    }

    private void displayDataInTable(JSONArray jsonArray) throws JSONException {
        String[] columnNames = {"_id", "user_id", "ticket", "type", "symbol", "volume", "reason", "price", "status", "closed", "stopLoss", "takeProfit", "profit", "comment", "expiration", "id", "swap", "comission", "createdAt", "updatedAt", "__v"};
        Object[][] rowData = new Object[jsonArray.length()][columnNames.length];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            for (int j = 0; j < columnNames.length; j++) {
                rowData[i][j] = jsonObject.opt(columnNames[j]);
            }
        }
        table = new JTable(rowData, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        JOptionPane.showMessageDialog(this, scrollPane, "Fetched Data", JOptionPane.PLAIN_MESSAGE);
    }

    private MaskFormatter createFormatter(String format) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(format);
            formatter.setPlaceholderCharacter('_'); // Placeholder character for empty spaces
        } catch (Exception exc) {
            System.err.println("Formatter is invalid: " + exc.getMessage());
        }
        return formatter;
    }

    // Main method to run the application
    public static void main(String[] args) {
        new Viewer();
    }
}

