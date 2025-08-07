package crud.clinica.view.exame;

import crud.clinica.controller.ExameListController;
import crud.clinica.facade.ClinicaFacade;

import javax.swing.*;
import java.awt.*;

public class ExameListDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JTable table;
    private final ExameListController controller;

    public ExameListDialog(Window parent, ClinicaFacade facade, boolean podeEditar, boolean podeExcluir) {
        super(parent, "Gerenciar Exames", ModalityType.APPLICATION_MODAL);

        // A View cria seu Controller, que conterá toda a lógica
        this.controller = new ExameListController(this, facade);

        setSize(800, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Tabela ---
        // Usando nosso novo e elegante ExameTableModel!
        table = new JTable(new ExameTableModel());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Painel de Botões ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnFechar = new JButton("Fechar");

        if (podeEditar) buttonPanel.add(btnEditar);
        if (podeExcluir) buttonPanel.add(btnExcluir);
        buttonPanel.add(btnFechar);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Listeners delegados ao Controller ---
        btnEditar.addActionListener(e -> controller.editarExame());
        btnExcluir.addActionListener(e -> controller.excluirExame());
        btnFechar.addActionListener(e -> dispose());

        // Listener para habilitar/desabilitar botões conforme a seleção
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean linhaSelecionada = table.getSelectedRow() != -1;
                btnEditar.setEnabled(linhaSelecionada && podeEditar);
                btnExcluir.setEnabled(linhaSelecionada && podeExcluir);
            }
        });
        
        // Desabilitar botões no início
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);

        // O Controller é quem carrega os dados
        controller.carregarDadosIniciais();
    }

    // Getter para que o Controller possa acessar a tabela
    public JTable getTable() {
        return table;
    }
}