package crud.clinica.app;

import crud.clinica.database.DatabaseManager;
import crud.clinica.database.IConnection;
import crud.clinica.facade.ClinicaFacade;
import crud.clinica.view.MainView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            // PASSO 1: Configurar o banco de dados (criar tabelas, etc.).
            // Isso roda apenas UMA VEZ na inicialização.
            DatabaseManager.setupDatabase();
            
            // PASSO 2: Obter a implementação da conexão.
            IConnection connection = new DatabaseManager.MySQLConnection();
            
            // PASSO 3: Criar a instância ÚNICA da nossa Façade.
            // Ela contém toda a lógica de negócio e acesso a dados.
            final ClinicaFacade facade = new ClinicaFacade(connection);
            
            // PASSO 4: Iniciar a interface gráfica, passando a Façade para a MainView.
            SwingUtilities.invokeLater(() -> {
                // Agora a MainView recebe a "chave" para todo o backend.
                MainView mainView = new MainView(facade); 
                mainView.setVisible(true);
            });

        } catch (Exception e) {
            // Se algo der errado na inicialização (ex: não conectar ao banco),
            // a aplicação mostrará um erro e não vai iniciar.
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Erro crítico ao iniciar a aplicação: \n" + e.getMessage(), 
                "Erro Fatal", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}