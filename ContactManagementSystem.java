import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactManagementSystem {
    private JFrame frame;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private List<Contact> contacts;

    public ContactManagementSystem() {
        contacts = new ArrayList<>();
        loadContacts();

        frame = new JFrame("Contact Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        JButton addButton = new JButton("Add Contact");
        addButton.addActionListener(new AddContactListener());
        inputPanel.add(addButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Name", "Phone", "Email"}, 0);
        contactTable = new JTable(tableModel);
        contactTable.setPreferredScrollableViewportSize(new Dimension(500, 300));
        contactTable.setFillsViewportHeight(true);

        JScrollPane tableScrollPane = new JScrollPane(contactTable);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new SaveListener());
        fileMenu.add(saveItem);
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ExitListener());
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.pack();
        frame.setVisible(true);
    }

    private void loadContacts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("contacts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Contact contact = new Contact(parts[0], parts[1], parts[2]);
                contacts.add(contact);
                tableModel.addRow(new Object[]{contact.getName(), contact.getPhone(), contact.getEmail()});
            }
        } catch (IOException e) {
            // Handle exception
        }
    }

    private void saveContacts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("contacts.txt"))) {
            for (Contact contact : contacts) {
                writer.write(contact.getName() + "," + contact.getPhone() + "," + contact.getEmail() + "\n");
            }
        } catch (IOException e) {
            // Handle exception
        }
    }

    private class AddContactListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();

            Contact contact = new Contact(name, phone, email);
            contacts.add(contact);
            tableModel.addRow(new Object[]{contact.getName(), contact.getPhone(), contact.getEmail()});

            nameField.setText("");
            phoneField.setText("");
            emailField.setText("");
        }
    }

    private class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveContacts();
        }
    }

    private class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveContacts();
            System.exit(0);
        }
    }

    private static class Contact {
        private String name;
        private String phone;
        private String email;

        public Contact(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ContactManagementSystem();
            }
        });
    }
}