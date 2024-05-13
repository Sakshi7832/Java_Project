package Fetching_Data;

import javax.swing.JFrame;
import javax.swing.text.MaskFormatter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DataViewer extends JFrame {
    private JFormattedTextField startDateField;
    private JFormattedTextField endDateField;
    private JButton fetchButton;

    public DataViewer() {
        setTitle("Data Viewer");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create formatted text fields for dates
        startDateField = new JFormattedTextField(createFormatter("####-##-##"));
        endDateField = new JFormattedTextField(createFormatter("####-##-##"));

        // Create fetch button
        fetchButton = new JButton("Fetch Data");

        // Add components to the frame
        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Start Date:"));
        formPanel.add(startDateField);
        formPanel.add(new JLabel("End Date:"));
        formPanel.add(endDateField);
        formPanel.add(fetchButton);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);

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

//        // Perform actions with entered dates (e.g., fetch data from API)
//        System.out.println("Start Date: " + startDate);
//        System.out.println("End Date: " + endDate);
//    
    
    // Fetch the data from the URL
        new Thread(() ->
    {
    	try {
    		URL url = new URL("https://piptrade.org/webtest/testingdata.json");
    		//URL url = uri.toURL();
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.setRequestMethod("GET");
    		
    		int responseCode = conn.getResponseCode();
    		if(responseCode==HttpURLConnection.HTTP_OK)
    		{
    			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    			String inputLine;
    			StringBuilder response = new StringBuilder();
    			
    			while((inputLine = in.readLine())!=null)
    			{
    				response.append(inputLine);
    			}
    			in.close();
    			
    			System.out.println("Fetched Data: "+response.toString());
    		}else
    		{
    			System.out.println("Failed to fetch the data");
    		}
    	}catch(Exception e)
    	{
    		System.out.println(e);
    	}
    }).start();
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
                     new DataViewer();
        
    }
}
