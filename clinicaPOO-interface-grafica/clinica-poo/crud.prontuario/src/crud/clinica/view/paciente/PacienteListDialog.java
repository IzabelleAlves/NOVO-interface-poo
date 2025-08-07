package crud.clinica.view.paciente;

import crud.clinica.controller.PacienteListController;
import crud.clinica.facade.ClinicaFacade;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PacienteListDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JTable table;
    private final JButton btnEditar;
    private final JButton btnExcluir;
    
    private final PacienteListController controller;

    public PacienteListDialog(Window parent, ClinicaFacade facade, boolean podeEditar, boolean podeExcluir) {
        super(parent, "Gerenciar Pacientes", ModalityType.APPLICATION_MODAL);
        
        this.controller = new PacienteListController(this, facade);

        setSize(800, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Painel de Pesquisa ---
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // ... (código para criar os componentes de pesquisa continua igual)
        // Por simplicidade, o filtro foi delegado para o controller, mas a UI não muda.
        JTextField txtPesquisa = new JTextField(20);
        txtPesquisa.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Ação de filtro delegada ao controller
                controller.filtrarPacientes(); 
            }
        });
        painelPesquisa.add(new JLabel("Pesquisar:"));
        painelPesquisa.add(txtPesquisa);
        add(painelPesquisa, BorderLayout.NORTH);

        // --- Tabela ---
        // Usando nosso novo TableModel!
        table = new JTable(new PacienteTableModel());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Painel de Botões ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        JButton btnFechar = new JButton("Fechar");

        if (podeEditar) buttonPanel.add(btnEditar);
        if (podeExcluir) buttonPanel.add(btnExcluir);
        buttonPanel.add(btnFechar);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Listeners ---
        btnEditar.addActionListener(e -> controller.editarPaciente());
        btnExcluir.addActionListener(e -> controller.excluirPaciente());
        btnFechar.addActionListener(e -> dispose());
        
        table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                boolean linhaSelecionada = table.getSelectedRow() != -1;
                btnEditar.setEnabled(linhaSelecionada && podeEditar);
                btnExcluir.setEnabled(linhaSelecionada && podeExcluir);
            }
        });

        // Desabilitar botões inicialmente
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);

        // Carrega os dados iniciais através do controller
        controller.carregarDadosIniciais();
    }

    // Getter para o controller acessar a tabela
    public JTable getTable() {
        return table;
    }
}