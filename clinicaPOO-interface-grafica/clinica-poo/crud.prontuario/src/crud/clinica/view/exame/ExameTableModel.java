package crud.clinica.view.exame;

import crud.clinica.model.Exame;
import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExameTableModel extends AbstractTableModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
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
               
                return (exame.getPaciente() != null) ? exame.getPaciente().getNome() : "Não informado";
            default: 
                return null;
        }
    }

    public Exame getExameAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < exames.size()) {
            return exames.get(rowIndex);
        }
        return null;
    }

    public void setExames(List<Exame> exames) {
        this.exames = exames;
        fireTableDataChanged(); 
    }
}