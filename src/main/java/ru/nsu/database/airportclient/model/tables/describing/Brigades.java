package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Brigades implements ITable {

    public final static String TABLE_NAME = "brigades";

    public final static String TABLE_NAME_ALIAS = "Бригады";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("brigade_id", true, TokenType.INTEGER, null, true, true, "Номер"),
            new Token("brigare_department_id", false,TokenType.INTEGER, null, true, true, "ИД департамента")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("brigade_id", true, TokenType.INTEGER, null, true, true, "Номер"),
            new Token("specialties_name", true, TokenType.VARCHAR, null, true, true, "Специализация")
    );

    public final static List<LinkedToken> ADD_TOKENS = List.of(
            new LinkedToken("brigare_department_id", "Отдел", false, TokenType.INTEGER, 0, "select department_id, specialties_name from departments join specialties on department_specialization = specialties_id where department_id = 1 or department_id = 3 or department_id = 4", Arrays.asList(
                    new Token("department_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
                    new Token("specialties_name", false, TokenType.VARCHAR, null, true, true, "Специализация")
            ))
    );

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<Token> getAllTokens() {
        return TOKENS;
    }

    @Override
    public String getTableAlias() {
        return TABLE_NAME_ALIAS;
    }

    @Override
    public List<Token> getDataTokens() {
        return DATA_TOKENS;
    }

    @Override
    public String getRequestForGetData() {
        return "select brigade_id, specialties_name from brigades" +
                " join departments on brigare_department_id = department_id join specialties on department_specialization = specialties_id";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return ADD_TOKENS;
    }

    @Override
    public String getSQLForAddData() {
        return "insert into brigades(brigare_department_id) values(?)";
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return null;
    }

    @Override
    public String getSQLForUpdate() {
        return null;
    }

    @Override
    public List<LinkedToken> getTokensForUpdate() {
        return null;
    }

    @Override
    public String getSQLForDelete() {
        return null;
    }
}
