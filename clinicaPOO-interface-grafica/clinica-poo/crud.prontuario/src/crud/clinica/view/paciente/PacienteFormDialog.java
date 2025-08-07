package crud.clinica.view.paciente;

import crud.clinica.controller.PacienteFormController; // Importa o controller
import crud.clinica.dao.PacienteDAO;
import crud.clinica.database.IConnection;
import crud.clinica.facade.ClinicaFacade;
import crud.clinica.model.Paciente;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;

public class PacienteFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    // --- Componentes da UI ---
    private final JTextField nomeField;
    private JFormattedTextField cpfField;
    private JFormattedTextField dataNascimentoField;

    // --- Controller ---
    private final PacienteFormController controller;

    public PacienteFormDialog(Window parent, ClinicaFacade facade, Paciente paciente) {
        super(parent, paciente == null ? "Cadastrar Paciente" : "Editar Paciente", ModalityType.APPLICATION_MODAL);
        
        // A View agora cria seu próprio Controller, passando as dependências para ele
        this.controller = new PacienteFormController(this, new PacienteDAO((IConnection) facade), paciente);

        setSize(350, 220);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(parent);
        setResizable(false);

        // --- Painel de Formulário ---
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adiciona um pouco de espaço

        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        formPanel.add(new JLabel("CPF:"));
        try {
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            cpfField = new JFormattedTextField(cpfMask);
        } catch (ParseException e) {
            cpfField = new JFormattedTextField(); // Fallback
        }
        formPanel.add(cpfField);

        formPanel.add(new JLabel("Data de Nascimento:"));
        try {
            MaskFormatter dataMask = new MaskFormatter("##/##/####");
            dataMask.setPlaceholderCharacter('_');
            dataNascimentoField = new JFormattedTextField(dataMask);
        } catch (ParseException e) {
            dataNascimentoField = new JFormattedTextField(); // Fallback
        }
        formPanel.add(dataNascimentoField);

        // --- Painel de Botões ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton(paciente == null ? "Salvar" : "Atualizar");
        buttonPanel.add(btnSalvar);

        if (paciente == null) {
            JButton btnLimpar = new JButton("Limpar");
            buttonPanel.add(btnLimpar);
            btnLimpar.addActionListener(e -> limparCampos());
        }

        JButton btnSair = new JButton("Sair");
        buttonPanel.add(btnSair);

        // --- Adicionando painéis à janela ---
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        // A ação agora é delegada para o método salvar do CONTROLLER
        btnSalvar.addActionListener(e -> controller.salvar());
        btnSair.addActionListener(e -> dispose());
        
        // Preenche os campos se estiver em modo de edição
        if (paciente != null) {
            preencherCampos(paciente);
        }
    }
    
    // --- Métodos de acesso para o Controller ---
    public String getNome() {
        return nomeField.getText().trim();
    }
    
    public String getCpf() {
        return cpfField.getText();
    }
    
    public String getDataNascimento() {
        return dataNascimentoField.getText();
    }

    // --- Métodos de manipulação da UI ---
    private void limparCampos() {
        nomeField.setText("");
        cpfField.setValue(null);
        dataNascimentoField.setValue(null);
    }
    
    private void preencherCampos(Paciente paciente) {
        nomeField.setText(paciente.getNome());
        cpfField.setText(paciente.getCpf());
        if (paciente.getDataNascimento() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dataNascimentoField.setText(paciente.getDataNascimento().format(formatter));
        }
    }
}