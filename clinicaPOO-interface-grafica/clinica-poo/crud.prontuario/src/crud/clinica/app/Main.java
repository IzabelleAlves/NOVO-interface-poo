package crud.clinica.app;

import crud.clinica.database.DatabaseManager;
import crud.clinica.database.IConnection;
import crud.clinica.facade.ClinicaFacade;
import crud.clinica.view.MainView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseManager.setupDatabase();
            IConnection connection = new DatabaseManager.MySQLConnection();
            final ClinicaFacade facade = new ClinicaFacade(connection);
            
            SwingUtilities.invokeLater(() -> {
                MainView mainView = new MainView(facade); 
                mainView.setVisible(true);
            });

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Erro crítico ao iniciar a aplicação: \n" + e.getMessage(), 
                "Erro Fatal", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}