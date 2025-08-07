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

    private final JComboBox<Paciente> pacienteComboBox;
    private JFormattedTextField dataField;
    private final JTextField descricaoField;

    private final ExameFormController controller;

    public ExameFormDialog(Window parent, ClinicaFacade facade, Exame exame) {
        super(parent, exame == null ? "Cadastrar Exame" : "Editar Exame", ModalityType.APPLICATION_MODAL);

        this.controller = new ExameFormController(this, facade, exame);

        setSize(400, 200);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(parent);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        formPanel.add(new JLabel("Paciente:"));
        pacienteComboBox = new JComboBox<>();
        pacienteComboBox.setToolTipText("Selecione o paciente para este exame"); 
        formPanel.add(pacienteComboBox);

        formPanel.add(new JLabel("Data (DD/MM/AAAA):"));
        try {
            MaskFormatter dataMask = new MaskFormatter("##/##/####");
            dataMask.setPlaceholderCharacter('_');
            dataField = new JFormattedTextField(dataMask);
        } catch (ParseException e) {
            dataField = new JFormattedTextField();
        }
        dataField.setToolTipText("Digite a data em que o exame foi realizado"); 
        formPanel.add(dataField);

        formPanel.add(new JLabel("Descrição:"));
        descricaoField = new JTextField();
        descricaoField.setToolTipText("Digite a descrição do exame (ex: Hemograma, Raio-X)"); 
        formPanel.add(descricaoField);
        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton(exame == null ? "Salvar" : "Atualizar");
        btnSalvar.setToolTipText("Salvar os dados do exame");
        
        JButton btnSair = new JButton("Sair");
        btnSair.setToolTipText("Fechar este formulário sem salvar");

        buttonPanel.add(btnSalvar);
        if (exame == null) {
            JButton btnLimpar = new JButton("Limpar");
            btnLimpar.setToolTipText("Limpar os campos para um novo cadastro");
            buttonPanel.add(btnLimpar);
            btnLimpar.addActionListener(e -> limparCampos());
        }
        buttonPanel.add(btnSair);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> controller.salvar());
        btnSair.addActionListener(e -> dispose());

        controller.inicializar();
        
        if (exame != null) {
            preencherCampos(exame);
        }
    }

    public void setPacientesNoComboBox(List<Paciente> pacientes) {
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
    
    private void preencherCampos(Exame exame) {
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