package dbfit.fixture;

import dbfit.api.DBEnvironment;
import dbfit.api.DbEnvironmentFactory;
import dbfit.util.DataTable;
import dbfit.util.FitNesseTestHost;
import fit.Parse;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class StoreQuery extends fit.Fixture {

    private DBEnvironment dbEnvironment;
    private String query;
    private String symbolName;
    private String[] symbols;

    public StoreQuery() {
        dbEnvironment = DbEnvironmentFactory.getDefaultEnvironment();
    }

    public StoreQuery(DBEnvironment environment, String query, String symbolName) {
        this.dbEnvironment = environment;
        this.query = query;
        this.symbolName = symbolName;
        this.symbols = new String[]{symbolName};
    }

    public StoreQuery(DBEnvironment environment, String query, String[] symbols) {
        this.dbEnvironment = environment;
        this.query = query;
        this.symbolName = (symbols != null && symbols.length > 0) ? symbols[0] : "";
        this.symbols = symbols;
    }

    public void doTable(Parse table) {
        if (query == null || symbolName == null || symbols == null) {
            if (args.length < 2) {
                throw new UnsupportedOperationException(
                        "No query and symbol name specified to StoreQuery constructor or argument list");
            }
            query = args[0];
            symbolName = args[1];
            symbols = Arrays.copyOfRange(args, 1, args.length);
        }

        try (
                PreparedStatement st =
                        dbEnvironment.createStatementWithBoundFixtureSymbols(
                                FitNesseTestHost.getInstance(), query)
        ) {
            if(symbols.length == 1 || symbols.length == 0){
                ResultSet rs = st.executeQuery();
                DataTable dt = new DataTable(rs);
                dbfit.util.SymbolUtil.setSymbol(symbolName, dt);
            } else{
                boolean result = st.execute();
                for (int i = 0; i < symbols.length; i++){

                    if(!result) {
                        throw new SQLException("Number of symbols ("+ symbols.length +") exceeds the number of result sets ("+ i +").");
                    }

                    ResultSet rs = st.getResultSet();
                    DataTable dt = new DataTable(rs);
                    dbfit.util.SymbolUtil.setSymbol(symbols[i], dt);

                    result = st.getMoreResults();
                }
            }

        } catch (SQLException sqle) {
            throw new Error(sqle);
        }
    }
}
