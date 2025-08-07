package crud.clinica.view.exame;

import crud.clinica.controller.ExameListController;
import crud.clinica.facade.ClinicaFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ExameListDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JTable table;
    private final JButton btnEditar;
    private final JButton btnExcluir;
    private final JTextField txtPesquisa;
    private final JRadioButton rbPaciente;
    private final JRadioButton rbDescricao;
    
    private final ExameListController controller;

    public ExameListDialog(Window parent, ClinicaFacade facade, boolean podeEditar, boolean podeExcluir) {
        super(parent, "Gerenciar Exames", ModalityType.APPLICATION_MODAL);
        
        setSize(800, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbPaciente = new JRadioButton("Nome do Paciente", true);
        rbDescricao = new JRadioButton("Descrição do Exame");
        ButtonGroup grupoRadio = new ButtonGroup();
        grupoRadio.add(rbPaciente);
        grupoRadio.add(rbDescricao);
        txtPesquisa = new JTextField(25);

        painelPesquisa.add(new JLabel("Pesquisar por:"));
        painelPesquisa.add(rbPaciente);
//        painelPesquisa.add(rbDescricao);
        painelPesquisa.add(txtPesquisa);
        add(painelPesquisa, BorderLayout.NORTH);


        table = new JTable(new ExameTableModel());
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
        

        this.controller = new ExameListController(this, facade);


        btnEditar.addActionListener(e -> controller.editarExame());
        btnExcluir.addActionListener(e -> controller.excluirExame());
        btnFechar.addActionListener(e -> dispose());

        txtPesquisa.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                controller.filtrarExames();
            }
        });
        rbPaciente.addActionListener(e -> controller.filtrarExames());
        rbDescricao.addActionListener(e -> controller.filtrarExames());
        
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
    public String getCriterioBusca() { return rbPaciente.isSelected() ? "paciente" : "descricao"; }
}