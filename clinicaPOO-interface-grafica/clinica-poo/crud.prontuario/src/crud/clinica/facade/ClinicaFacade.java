package crud.clinica.facade;

import java.util.List;
import crud.clinica.dao.ExameDAO;
import crud.clinica.dao.PacienteDAO;
import crud.clinica.database.IConnection;
import crud.clinica.model.Exame;
import crud.clinica.model.Paciente;

public class ClinicaFacade {

    private final PacienteDAO pacienteDAO;
    private final ExameDAO exameDAO;

    public ClinicaFacade(IConnection dbConnection) {
        this.pacienteDAO = new PacienteDAO(dbConnection);
        this.exameDAO = new ExameDAO(dbConnection);
    }

    // --- MÉTODOS PACIENTE ---
    public void salvarPaciente(Paciente paciente) throws Exception {
        if (paciente.getId() == null || paciente.getId() == 0) {
            pacienteDAO.create(paciente);
        } else {
            pacienteDAO.update(paciente);
        }
    }
    
    public void deletarPaciente(long pacienteId) throws Exception {
        pacienteDAO.delete(pacienteId);
    }
    
    public List<Paciente> listarPacientesComContagemDeExames() throws Exception {
        List<Paciente> pacientes = pacienteDAO.findAll();
        for (Paciente p : pacientes) {
            int qtdExames = exameDAO.countByPacienteId(p.getId());
            p.setQuantidadeExames(qtdExames);
        }
        return pacientes;
    }

    public Paciente buscarPacientePorId(long id) throws Exception {
        return pacienteDAO.findById(id);
    }

    public List<Paciente> listarTodosPacientes() throws Exception {
        return pacienteDAO.findAll();
    }

    // --- MÉTODOS EXAME ---
    public void salvarExame(Exame exame) throws Exception {
         if (exame.getId() == null || exame.getId() == 0) {
            exameDAO.create(exame);
        } else {
            exameDAO.update(exame);
        }
    }

    public List<Exame> listarTodosExames() throws Exception {
        return exameDAO.findAll();
    }
    
    public List<Exame> buscarExamesPorNomePaciente(String nome) throws Exception {
        return exameDAO.findByNomePaciente(nome);
    }
    
    public void deletarExame(long exameId) throws Exception {
        // 1. Cria um objeto Exame temporário.
        Exame exameParaDeletar = new Exame();
        
        // 2. Define o ID neste objeto.
        exameParaDeletar.setId(exameId);
        
        // 3. Passa o OBJETO para o método delete do DAO, como ele espera.
        exameDAO.delete(exameParaDeletar);
    }
    
 // Dentro da classe ClinicaFacade

    /**
     * Busca pacientes por um termo e critério, e enriquece os resultados com a contagem de exames.
     * @param termo O texto a ser buscado.
     * @param criterio A coluna onde buscar ("nome" ou "cpf").
     * @return Uma lista de pacientes filtrada.
     * @throws Exception se ocorrer um erro.
     */
    public List<Paciente> buscarPacientes(String termo, String criterio) throws Exception {
        List<Paciente> pacientes = pacienteDAO.findBy(termo, criterio);
        for (Paciente p : pacientes) {
            int qtdExames = exameDAO.countByPacienteId(p.getId());
            p.setQuantidadeExames(qtdExames);
        }
        return pacientes;
    }
    
 // Dentro da classe ClinicaFacade, na seção de Métodos para Exame

    /**
     * Busca exames por um termo e critério.
     * @param termo O texto a ser buscado.
     * @param criterio A coluna onde buscar ("paciente" ou "descricao").
     * @return Uma lista de exames filtrada.
     * @throws Exception se ocorrer um erro.
     */
    public List<Exame> buscarExames(String termo, String criterio) throws Exception {
        return exameDAO.findBy(termo, criterio);
    }
}