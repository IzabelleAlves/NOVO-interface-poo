package crud.clinica.controller;

import crud.clinica.facade.ClinicaFacade;
import crud.clinica.model.Paciente;
import crud.clinica.view.paciente.PacienteFormDialog;
import crud.clinica.view.paciente.PacienteListDialog;
import crud.clinica.view.paciente.PacienteTableModel;
import crud.clinica.view.util.DialogManager;

import java.util.List;

public class PacienteListController {

    private final PacienteListDialog view;
    private final ClinicaFacade facade;
    private final PacienteTableModel tableModel;

    public PacienteListController(PacienteListDialog view, ClinicaFacade facade) {
        this.view = view;
        this.facade = facade;
        this.tableModel = (PacienteTableModel) this.view.getTable().getModel();
    }

    public void carregarDadosIniciais() {
        try {
            // Usa o método da facade que já busca a contagem de exames
            List<Paciente> pacientes = facade.listarPacientesComContagemDeExames();
            tableModel.setPacientes(pacientes);
        } catch (Exception e) {
            DialogManager.showError(view, "Erro ao carregar pacientes: " + e.getMessage());
        }
    }

    public void excluirPaciente() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow == -1) {
            DialogManager.showError(view, "Selecione um paciente para excluir.");
            return;
        }

        if (DialogManager.showConfirm(view, "Deseja realmente excluir este paciente? Todos os seus exames também serão removidos.")) {
            try {
                Paciente paciente = tableModel.getPacienteAt(selectedRow);
                facade.deletarPaciente(paciente.getId());
                DialogManager.showSuccess(view, "Paciente excluído com sucesso.");
                carregarDadosIniciais(); // Recarrega a lista
            } catch (Exception e) {
                DialogManager.showError(view, "Erro ao excluir paciente: " + e.getMessage());
            }
        }
    }

    public void editarPaciente() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow == -1) {
            DialogManager.showError(view, "Selecione um paciente para editar.");
            return;
        }
        
        Paciente paciente = tableModel.getPacienteAt(selectedRow);
        
        // Abre o formulário de edição, passando a facade e o paciente selecionado
        PacienteFormDialog form = new PacienteFormDialog(view, facade, paciente);
        form.setVisible(true);
        
        // Após o formulário fechar, recarrega os dados para ver as atualizações
        carregarDadosIniciais();
    }
    
    // Este método deveria ser implementado na Facade/DAO para eficiência
    public void filtrarPacientes() {
        // Por simplicidade, manteremos o filtro em memória por enquanto,
        // mas o ideal seria a facade ter um método facade.buscarPaciente(termo, criterio)
        carregarDadosIniciais(); // Em uma versão futura, isso seria substituído
    }
}