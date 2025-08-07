package crud.clinica.controller;

import crud.clinica.facade.ClinicaFacade;
import crud.clinica.model.Exame;
import crud.clinica.model.Paciente;
import crud.clinica.view.exame.ExameFormDialog;
import crud.clinica.view.util.DialogManager;
import crud.clinica.view.util.ValidacaoUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ExameFormController {

    private final ExameFormDialog view;
    private final ClinicaFacade facade;
    private Exame exame; // O exame sendo criado ou editado

    public ExameFormController(ExameFormDialog view, ClinicaFacade facade, Exame exame) {
        this.view = view;
        this.facade = facade;
        this.exame = exame;
    }

    /**
     * Carrega os dados iniciais necessários para a tela, como a lista de pacientes.
     */
    public void inicializar() {
        try {
            List<Paciente> pacientes = facade.listarTodosPacientes();
            view.setPacientesNoComboBox(pacientes);
        } catch (Exception e) {
            DialogManager.showError(view, "Erro ao carregar a lista de pacientes: " + e.getMessage());
        }
    }

    /**
     * Valida e salva o exame.
     */
    public void salvar() {
        // 1. Obter dados da View
        Paciente pacienteSelecionado = view.getPacienteSelecionado();
        String dataStr = view.getData();
        String descricao = view.getDescricao();

        // 2. Validar os dados
        if (pacienteSelecionado == null) {
            DialogManager.showError(view, "Selecione um paciente.");
            return;
        }
        if (descricao.trim().isEmpty()) {
            DialogManager.showError(view, "O campo 'Descrição' é obrigatório.");
            return;
        }
        if (!ValidacaoUtil.validarData(dataStr)) {
            DialogManager.showError(view, "A data informada é inválida. Use o formato DD/MM/AAAA.");
            return;
        }

        // 3. Converter e preparar o Model
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataExame = LocalDate.parse(dataStr, formatter);

            // 4. Decidir entre criar ou atualizar
            if (exame == null) { // Modo Criação
                exame = new Exame(descricao, dataExame);
                exame.setPaciente(pacienteSelecionado);
            } else { // Modo Edição
                exame.setDescricao(descricao);
                exame.setData_exame(dataExame);
                exame.setPaciente(pacienteSelecionado);
            }

            // 5. Chamar a Facade para salvar
            facade.salvarExame(exame);
            DialogManager.showSuccess(view, "Exame salvo com sucesso!");
            view.dispose();

        } catch (DateTimeParseException e) {
            DialogManager.showError(view, "Erro no formato da data. Certifique-se que é DD/MM/AAAA.");
        } catch (Exception e) {
            DialogManager.showError(view, "Ocorreu um erro ao salvar o exame: " + e.getMessage());
            e.printStackTrace();
        }
    }
}