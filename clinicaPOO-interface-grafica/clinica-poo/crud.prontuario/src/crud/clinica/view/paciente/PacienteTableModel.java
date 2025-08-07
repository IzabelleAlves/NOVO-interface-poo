package crud.clinica.view.paciente;

import crud.clinica.model.Paciente;
import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PacienteTableModel extends AbstractTableModel {

    private final String[] colunas = {"ID", "Nome", "CPF", "Data de Nascimento", "Qtd. Exames"};
    private List<Paciente> pacientes;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PacienteTableModel() {
        this.pacientes = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return pacientes.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Paciente paciente = pacientes.get(rowIndex);
        switch (columnIndex) {
            case 0: return paciente.getId();
            case 1: return paciente.getNome();
            case 2: return paciente.getCpf();
            case 3: return paciente.getDataNascimento() != null ? paciente.getDataNascimento().format(formatter) : "";
            case 4: return paciente.getQuantidadeExames();
            default: return null;
        }
    }

    public Paciente getPacienteAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < pacientes.size()) {
            return pacientes.get(rowIndex);
        }
        return null;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
        fireTableDataChanged(); 
    }
}