package crud.clinica.view.exame;

import crud.clinica.model.Exame;
import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExameTableModel extends AbstractTableModel {

    // Define as colunas da tabela
    private final String[] colunas = {"ID", "Descrição", "Data do Exame", "Paciente"};
    private List<Exame> exames;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ExameTableModel() {
        this.exames = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return exames.size();
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
        Exame exame = exames.get(rowIndex);
        switch (columnIndex) {
            case 0: return exame.getId();
            case 1: return exame.getDescricao();
            case 2: return exame.getData_exame() != null ? exame.getData_exame().format(formatter) : "";
            case 3: 
                // Retorna o nome do paciente. O objeto Paciente dentro de Exame deve estar preenchido.
                return (exame.getPaciente() != null) ? exame.getPaciente().getNome() : "Não informado";
            default: 
                return null;
        }
    }

    /**
     * Retorna o objeto Exame completo da linha selecionada.
     * @param rowIndex O índice da linha.
     * @return O objeto Exame correspondente.
     */
    public Exame getExameAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < exames.size()) {
            return exames.get(rowIndex);
        }
        return null;
    }

    /**
     * Atualiza a lista de exames na tabela e notifica a JTable da mudança.
     * @param exames A nova lista de exames a ser exibida.
     */
    public void setExames(List<Exame> exames) {
        this.exames = exames;
        fireTableDataChanged(); // Essencial para atualizar a JTable
    }
}