package crud.clinica.view.paciente;

import crud.clinica.controller.PacienteFormController;
import crud.clinica.facade.ClinicaFacade;
import crud.clinica.model.Paciente;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;

public class PacienteFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JTextField nomeField;
    private JFormattedTextField cpfField;
    private JFormattedTextField dataNascimentoField;

    private final PacienteFormController controller;

    public PacienteFormDialog(Window parent, ClinicaFacade facade, Paciente paciente) {
        super(parent, paciente == null ? "Cadastrar Paciente" : "Editar Paciente", ModalityType.APPLICATION_MODAL);
        
        this.controller = new PacienteFormController(this, facade, paciente);

        setSize(350, 220);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(parent);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PAINEL DO FORMULÁRIO ---
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        nomeField.setToolTipText("Digite o nome completo do paciente"); 
        formPanel.add(nomeField);

        formPanel.add(new JLabel("CPF:"));
        try {
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            cpfField = new JFormattedTextField(cpfMask);
        } catch (ParseException e) {
            cpfField = new JFormattedTextField();
        }
        cpfField.setToolTipText("Digite o CPF do paciente");
        formPanel.add(cpfField);

        formPanel.add(new JLabel("Data de Nascimento:"));
        try {
            MaskFormatter dataMask = new MaskFormatter("##/##/####");
            dataMask.setPlaceholderCharacter('_');
            dataNascimentoField = new JFormattedTextField(dataMask);
        } catch (ParseException e) {
            dataNascimentoField = new JFormattedTextField();
        }
        dataNascimentoField.setToolTipText("Digite a data no formato Dia/Mês/Ano"); 
        formPanel.add(dataNascimentoField);
        add(formPanel, BorderLayout.CENTER);

        // --- PAINEL DE BOTÕES ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton(paciente == null ? "Salvar" : "Atualizar");
        btnSalvar.setToolTipText("Gravar os dados do paciente no sistema"); 
        
        JButton btnSair = new JButton("Sair");
        btnSair.setToolTipText("Fechar o formulário sem salvar");

        buttonPanel.add(btnSalvar);
        if (paciente == null) {
            JButton btnLimpar = new JButton("Limpar");
            btnLimpar.setToolTipText("Limpar todos os campos preenchidos");
            buttonPanel.add(btnLimpar);
            btnLimpar.addActionListener(e -> limparCampos());
        }
        buttonPanel.add(btnSair);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> controller.salvar());
        btnSair.addActionListener(e -> dispose());
        
        if (paciente != null) {
            preencherCampos(paciente);
        }
    }
    
    public String getNome() { return nomeField.getText().trim(); }
    public String getCpf() { return cpfField.getText(); }
    public String getDataNascimento() { return dataNascimentoField.getText(); }

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