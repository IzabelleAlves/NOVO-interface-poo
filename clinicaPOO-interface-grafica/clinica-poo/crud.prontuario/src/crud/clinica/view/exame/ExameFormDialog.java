package crud.clinica.view.exame;

import crud.clinica.controller.ExameFormController;
import crud.clinica.facade.ClinicaFacade;
import crud.clinica.model.Exame;
import crud.clinica.model.Paciente;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExameFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    // --- Componentes da UI ---
    private final JComboBox<Paciente> pacienteComboBox;
    private JFormattedTextField dataField;
    private final JTextField descricaoField;

    // --- Controller ---
    private final ExameFormController controller;

    // O construtor agora é limpo: recebe a facade e o objeto (se for edição)
    public ExameFormDialog(Window parent, ClinicaFacade facade, Exame exame) {
        super(parent, exame == null ? "Cadastrar Exame" : "Editar Exame", ModalityType.APPLICATION_MODAL);

        // A View cria seu próprio controller
        this.controller = new ExameFormController(this, facade, exame);

        setSize(400, 200);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(parent);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Painel do Formulário ===
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.add(new JLabel("Paciente:"));
        pacienteComboBox = new JComboBox<>();
        formPanel.add(pacienteComboBox);

        formPanel.add(new JLabel("Data (DD/MM/AAAA):"));
        try {
            MaskFormatter dataMask = new MaskFormatter("##/##/####");
            dataMask.setPlaceholderCharacter('_');
            dataField = new JFormattedTextField(dataMask);
        } catch (ParseException e) {
            dataField = new JFormattedTextField();
        }
        formPanel.add(dataField);

        formPanel.add(new JLabel("Descrição:"));
        descricaoField = new JTextField();
        formPanel.add(descricaoField);
        add(formPanel, BorderLayout.CENTER);

        // === Painel de Botões ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton(exame == null ? "Salvar" : "Atualizar");
        JButton btnSair = new JButton("Sair");

        buttonPanel.add(btnSalvar);
        if (exame == null) {
            JButton btnLimpar = new JButton("Limpar");
            buttonPanel.add(btnLimpar);
            btnLimpar.addActionListener(e -> limparCampos());
        }
        buttonPanel.add(btnSair);
        add(buttonPanel, BorderLayout.SOUTH);

        // === Ações delegadas ao Controller ===
        btnSalvar.addActionListener(e -> controller.salvar());
        btnSair.addActionListener(e -> dispose());

        // Carrega os dados iniciais (pacientes no combobox)
        controller.inicializar();
        
        // Pré-preenchimento se for edição
        if (exame != null) {
            preencherCampos(exame);
        }
    }

    // --- Métodos para o Controller usar ---

    public void setPacientesNoComboBox(List<Paciente> pacientes) {
        // Limpa o combo box antes de adicionar os novos itens
        pacienteComboBox.removeAllItems();
        for (Paciente p : pacientes) {
            pacienteComboBox.addItem(p);
        }
    }

    public Paciente getPacienteSelecionado() {
        return (Paciente) pacienteComboBox.getSelectedItem();
    }

    public String getData() {
        return dataField.getText();
    }

    public String getDescricao() {
        return descricaoField.getText();
    }

    // --- Métodos internos da View ---
    
    private void preencherCampos(Exame exame) {
        // Para selecionar o item correto, precisamos iterar, pois a instância pode ser diferente
        for (int i = 0; i < pacienteComboBox.getItemCount(); i++) {
            if (pacienteComboBox.getItemAt(i).getId().equals(exame.getPaciente().getId())) {
                pacienteComboBox.setSelectedIndex(i);
                break;
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataField.setText(exame.getData_exame().format(formatter));
        descricaoField.setText(exame.getDescricao());
    }

    private void limparCampos() {
        if (pacienteComboBox.getItemCount() > 0) {
            pacienteComboBox.setSelectedIndex(0);
        }
        dataField.setValue(null);
        descricaoField.setText("");
    }
}