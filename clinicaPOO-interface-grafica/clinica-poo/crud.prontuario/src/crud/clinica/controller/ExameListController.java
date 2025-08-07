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
        // Pega a referência do TableModel que está na View
        this.tableModel = (ExameTableModel) this.view.getTable().getModel();
    }

    /**
     * Carrega a lista inicial de exames do banco de dados e atualiza a tabela.
     */
    public void carregarDadosIniciais() {
        try {
            List<Exame> exames = facade.listarTodosExames();
            tableModel.setExames(exames);
        } catch (Exception e) {
            DialogManager.showError(view, "Erro ao carregar a lista de exames: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Exclui o exame selecionado na tabela.
     */
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
                carregarDadosIniciais(); // Recarrega a lista para refletir a exclusão
            } catch (Exception e) {
                DialogManager.showError(view, "Erro ao excluir o exame: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Abre a janela de formulário para editar o exame selecionado.
     */
    public void editarExame() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow == -1) {
            DialogManager.showError(view, "Selecione um exame para editar.");
            return;
        }
        
        Exame exame = tableModel.getExameAt(selectedRow);
        
        // Abre o formulário de edição, passando a facade e o exame selecionado
        ExameFormDialog form = new ExameFormDialog(view, facade, exame);
        form.setVisible(true);
        
        // Após o formulário de edição fechar, recarrega os dados para ver as atualizações
        carregarDadosIniciais();
    }
    
 // Dentro da classe ExameListController

 // Adicione este novo método
 /**
  * Filtra a lista de exames com base nos dados da view.
  */
 public void filtrarExames() {
     String termo = view.getTermoBusca();
     String criterio = view.getCriterioBusca();

     try {
         List<Exame> exames;
         if (termo.trim().isEmpty()) {
             // Se a busca estiver vazia, carrega todos os exames
             exames = facade.listarTodosExames();
         } else {
             // Senão, busca no banco com o filtro
             exames = facade.buscarExames(termo, criterio);
         }
         tableModel.setExames(exames);
     } catch (Exception e) {
         DialogManager.showError(view, "Erro ao filtrar exames: " + e.getMessage());
     }
 }
}