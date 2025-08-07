package crud.clinica.controller;

import crud.clinica.facade.ClinicaFacade;
import crud.clinica.model.Exame;
import crud.clinica.view.exame.ExameFormDialog;
import crud.clinica.view.exame.ExameListDialog;
import crud.clinica.view.exame.ExameTableModel;
import crud.clinica.view.util.DialogManager;

import java.util.List;

public class ExameListController {

    private final ExameListDialog view;
    private final ClinicaFacade facade;
    private final ExameTableModel tableModel;

    public ExameListController(ExameListDialog view, ClinicaFacade facade) {
        this.view = view;
        this.facade = facade;

        this.tableModel = (ExameTableModel) this.view.getTable().getModel();
    }

    public void carregarDadosIniciais() {
        try {
            List<Exame> exames = facade.listarTodosExames();
            tableModel.setExames(exames);
        } catch (Exception e) {
            DialogManager.showError(view, "Erro ao carregar a lista de exames: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void excluirExame() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow == -1) {
            DialogManager.showError(view, "Selecione um exame para excluir.");
            return;
        }

        if (DialogManager.showConfirm(view, "Deseja realmente excluir este exame?")) {
            try {
                Exame exame = tableModel.getExameAt(selectedRow);
                facade.deletarExame(exame.getId());
                DialogManager.showSuccess(view, "Exame excluído com sucesso.");
                carregarDadosIniciais(); 
            } catch (Exception e) {
                DialogManager.showError(view, "Erro ao excluir o exame: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public void editarExame() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow == -1) {
            DialogManager.showError(view, "Selecione um exame para editar.");
            return;
        }
        
        Exame exame = tableModel.getExameAt(selectedRow);
        
        ExameFormDialog form = new ExameFormDialog(view, facade, exame);
        form.setVisible(true);
        
        carregarDadosIniciais();
    }

 public void filtrarExames() {
     String termo = view.getTermoBusca();
     String criterio = view.getCriterioBusca();

     try {
         List<Exame> exames;
         if (termo.trim().isEmpty()) {
             exames = facade.listarTodosExames();
         } else {
             exames = facade.buscarExames(termo, criterio);
         }
         tableModel.setExames(exames);
     } catch (Exception e) {
         DialogManager.showError(view, "Erro ao filtrar exames: " + e.getMessage());
     }
 }
}