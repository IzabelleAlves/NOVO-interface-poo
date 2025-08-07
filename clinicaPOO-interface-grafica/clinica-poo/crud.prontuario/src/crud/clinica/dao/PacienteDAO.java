package crud.clinica.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import crud.clinica.database.IConnection;
import crud.clinica.exception.CPFJaExisteException;
import crud.clinica.model.Paciente;

public class PacienteDAO implements IEntityDAO<Paciente> {

    private final IConnection conn;

    public PacienteDAO(IConnection dbConnection) {
        this.conn = dbConnection;
    }

    @Override
    public void create(Paciente t) throws SQLException, CPFJaExisteException {
        // 1. A verificação agora funciona, pois o método existsByCPF() está implementado.
        if (existsByCPF(t.getCpf())) {
            throw new CPFJaExisteException("Já existe um paciente com o CPF informado.");
        }

        // 2. Se não houver duplicidade, a inserção é feita.
        String sql = "INSERT INTO PACIENTES (nome, cpf, data_nascimento) VALUES (?, ?, ?);";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql)) {
            pstm.setString(1, t.getNome());
            pstm.setString(2, t.getCpf());
            pstm.setTimestamp(3, Timestamp.valueOf(t.getDataNascimento()));
            pstm.execute();
        }
    }

    @Override
    public void update(Paciente t) throws SQLException, CPFJaExisteException {
        // A verificação de CPF duplicado para OUTRO paciente foi implementada aqui.
        String sqlCheck = "SELECT id FROM pacientes WHERE cpf = ? AND id != ? LIMIT 1;";
        try (PreparedStatement pstmCheck = conn.getConnection().prepareStatement(sqlCheck)) {
            pstmCheck.setString(1, t.getCpf());
            pstmCheck.setLong(2, t.getId());
            try (ResultSet rs = pstmCheck.executeQuery()) {
                if (rs.next()) {
                    throw new CPFJaExisteException("Já existe outro paciente com o CPF informado.");
                }
            }
        }

        String sqlUpdate = "UPDATE PACIENTES SET NOME = ?, CPF = ?, data_nascimento = ? WHERE ID = ?;";
        try (PreparedStatement pstmUpdate = conn.getConnection().prepareStatement(sqlUpdate)) {
            pstmUpdate.setString(1, t.getNome());
            pstmUpdate.setString(2, t.getCpf());
            pstmUpdate.setTimestamp(3, Timestamp.valueOf(t.getDataNascimento()));
            pstmUpdate.setLong(4, t.getId());
            pstmUpdate.execute();
        }
    }

    // MÉTODO AUXILIAR TOTALMENTE IMPLEMENTADO
    public boolean existsByCPF(String cpf) throws SQLException {
        String sql = "SELECT 1 FROM pacientes WHERE cpf = ? LIMIT 1;";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql)) {
            pstm.setString(1, cpf);
            try (ResultSet rs = pstm.executeQuery()) {
                return rs.next(); // Retorna true se encontrou um registro, false caso contrário
            }
        }
    }
    
    @Override
    public Paciente findById(Long id) throws SQLException {
        Paciente p = null;
        String sql = "SELECT * FROM PACIENTES WHERE ID = ?;";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql)) {
            pstm.setLong(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    p = new Paciente(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getTimestamp("data_nascimento").toLocalDateTime()
                    );
                }
            }
        }
        return p;
    }

    @Override
    public List<Paciente> findAll() throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM PACIENTES ORDER BY nome;";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                pacientes.add(new Paciente(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getTimestamp("data_nascimento").toLocalDateTime()
                ));
            }
        }
        return pacientes;
    }

    public List<Paciente> findBy(String termo, String criterio) throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        
        if (!criterio.equalsIgnoreCase("nome") && !criterio.equalsIgnoreCase("cpf")) {
            throw new IllegalArgumentException("Critério de busca inválido.");
        }

        String sql = "SELECT * FROM pacientes WHERE " + criterio + " LIKE ? ORDER BY nome;";
        
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql)) {
            pstm.setString(1, "%" + termo + "%");
            
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    pacientes.add(new Paciente(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getTimestamp("data_nascimento").toLocalDateTime()
                    ));
                }
            }
        }
        return pacientes;
    }
    
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM PACIENTES WHERE ID = ?;";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql)) {
            pstm.setLong(1, id);
            pstm.execute();
        }
    }
    
    @Override
    public void delete(Paciente t) throws Exception {
       this.delete(t.getId());
    }
}