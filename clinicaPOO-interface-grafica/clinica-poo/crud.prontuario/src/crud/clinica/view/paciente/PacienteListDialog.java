package crud.clinica.view.paciente;

import crud.clinica.controller.PacienteListController;
import crud.clinica.facade.ClinicaFacade;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PacienteListDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JTable table;
    private final JButton btnEditar;
    private final JButton btnExcluir;
    private final JTextField txtPesquisa;
    private final JRadioButton rbNome;
    private final JRadioButton rbCpf;
    
    private final PacienteListController controller;

    public PacienteListDialog(Window parent, ClinicaFacade facade, boolean podeEditar, boolean podeExcluir) {
        super(parent, "Gerenciar Pacientes", ModalityType.APPLICATION_MODAL);
        
        setSize(800, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbNome = new JRadioButton("Nome", true);
        rbCpf = new JRadioButton("CPF");
        ButtonGroup grupoRadio = new ButtonGroup();
        grupoRadio.add(rbNome);
        grupoRadio.add(rbCpf);
        txtPesquisa = new JTextField(25);

        painelPesquisa.add(new JLabel("Pesquisar por:"));
        painelPesquisa.add(rbNome);
        painelPesquisa.add(rbCpf);
        painelPesquisa.add(txtPesquisa);
        add(painelPesquisa, BorderLayout.NORTH);

        table = new JTable(new PacienteTableModel());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        JButton btnFechar = new JButton("Fechar");

        if (podeEditar) buttonPanel.add(btnEditar);
        if (podeExcluir) buttonPanel.add(btnExcluir);
        buttonPanel.add(btnFechar);
        add(buttonPanel, BorderLayout.SOUTH);

        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);

        this.controller = new PacienteListController(this, facade);

        btnEditar.addActionListener(e -> controller.editarPaciente());
        btnExcluir.addActionListener(e -> controller.excluirPaciente());
        btnFechar.addActionListener(e -> dispose());
        
        txtPesquisa.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                controller.filtrarPacientes();
            }
        });
        rbNome.addActionListener(e -> controller.filtrarPacientes());
        rbCpf.addActionListener(e -> controller.filtrarPacientes());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean linhaSelecionada = table.getSelectedRow() != -1;
                btnEditar.setEnabled(linhaSelecionada && podeEditar);
                btnExcluir.setEnabled(linhaSelecionada && podeExcluir);
            }
        });
        
        controller.carregarDadosIniciais();
    }

    public JTable getTable() { return table; }
    public String getTermoBusca() { return txtPesquisa.getText(); }
    public String getCriterioBusca() { return rbNome.isSelected() ? "nome" : "cpf"; }
}