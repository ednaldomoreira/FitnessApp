package elsm.app.fitinbody;

import java.util.HashMap;  

import android.content.ContentProvider;  
import android.content.ContentUris;  
import android.content.Context;  
import android.content.UriMatcher;  
import android.net.Uri;  
import android.provider.BaseColumns;  
import android.util.Log;
import android.content.ContentValues;  
import android.database.Cursor;  
import android.database.sqlite.SQLiteDatabase;  
import android.database.sqlite.SQLiteOpenHelper;  
import android.database.sqlite.SQLiteQueryBuilder;  

public class ProgramasProvider extends ContentProvider {
    // Authority do nosso provider, a ser usado nas Uris.  
    public static final String AUTHORITY =  
        "elsm.app.fitinbody.programasprovider";  
  
    // Nome do arquivo que irá conter o banco de dados.  
    private static  final String DATABASE_NAME = "fb.db";  
  
    // Versao do banco de dados.  
    // Este valor é importante pois é usado em futuros updates do DB.  
    private static  final int  DATABASE_VERSION = 1;  
  
    // Nome da tabela que irá conter as anotações.  
    private static final  String PROGRAMAS_TABLE = "programas";  
    private static final  String TREINOS_TABLE = "treinos";
    private static final  String AVALIACOES_TABLE = "avaliacoes";
    private static final  String EXERCICIOS_TABLE = "exercicios";
    
  
    // 'Id' da Uri referente às notas do usuário.  
    private  static final int PROGRAMAS = 1;  
    private  static final int TREINOS = 2;  
    private  static final int AVALIACOES = 3;  
    private  static final int EXERCICIOS = 4;  
  
    // Tag usada para imprimir os logs.  
    public static final String TAG = "ProgramasProvider";  
  
    // Instância da classe utilitária  
    private DBHelper mHelper;  
  
    // Uri matcher - usado para extrair informações das Uris  
    private static final UriMatcher mMatcher;  
  
    private static HashMap<String, String> mProjectionPrograma;  
    private static HashMap<String, String> mProjectionTreino;  
    private static HashMap<String, String> mProjectionAvaliacao;  
    private static HashMap<String, String> mProjectionExercicios;  
      
    static {  
        mProjectionPrograma = new HashMap<String, String>(); 
        mProjectionTreino = new HashMap<String, String>();
        mProjectionAvaliacao = new HashMap<String, String>();
        mProjectionExercicios = new HashMap<String, String>();
        mProjectionPrograma.put(Programas.PROGRAMA_ID, Programas.PROGRAMA_ID);        
        mProjectionPrograma.put(Programas.DT_INICIO, Programas.DT_INICIO);  
        mProjectionPrograma.put(Programas.DT_FIM, Programas.DT_FIM);  
        mProjectionPrograma.put(Programas.NUM_DIAS, Programas.NUM_DIAS);  
        mProjectionPrograma.put(Programas.PESO, Programas.PESO);
        mProjectionPrograma.put(Programas.NUM_SERIES, Programas.NUM_SERIES);
        mProjectionPrograma.put(Programas.NUM_REPETICOES, Programas.NUM_REPETICOES);
        mProjectionPrograma.put(Programas.FREQUENCIA, Programas.FREQUENCIA);        
        mProjectionPrograma.put(Programas.OBJETIVO, Programas.OBJETIVO);
        mProjectionPrograma.put(Programas.OBSERVACAO, Programas.OBSERVACAO);  
        mProjectionPrograma.put(Programas.ABDOMINAL, Programas.ABDOMINAL);
	    mProjectionPrograma.put(Programas.AEROBICO, Programas.AEROBICO);
	    mProjectionPrograma.put(Programas.PEITORAIS, Programas.PEITORAIS);
	    mProjectionPrograma.put(Programas.COSTAS, Programas.COSTAS);
	    mProjectionPrograma.put(Programas.OMBROS, Programas.OMBROS);
	    mProjectionPrograma.put(Programas.TRICEPS, Programas.TRICEPS);
	    mProjectionPrograma.put(Programas.BICEPS, Programas.BICEPS);    
	    mProjectionPrograma.put(Programas.ANT_COXA, Programas.ANT_COXA);
	    mProjectionPrograma.put(Programas.POST_COXA, Programas.POST_COXA);
	    mProjectionPrograma.put(Programas.GLUTEOS, Programas.GLUTEOS);
	    mProjectionPrograma.put(Programas.ADUTORES, Programas.ADUTORES);
	    mProjectionPrograma.put(Programas.PANTURRILHA, Programas.PANTURRILHA);
        mProjectionTreino.put(Treinos.TREINO_ID, Treinos.TREINO_ID);        
        mProjectionTreino.put(Treinos.PROGRAMA_ID, Treinos.PROGRAMA_ID);        
        mProjectionTreino.put(Treinos.DT_TREINO, Treinos.DT_TREINO);  
        mProjectionTreino.put(Treinos.NUM_SESSAO, Treinos.NUM_SESSAO);  
        mProjectionTreino.put(Treinos.NUM_TREINO, Treinos.NUM_TREINO);
        mProjectionTreino.put(Treinos.OBSERVACAO, Treinos.OBSERVACAO);  
        mProjectionTreino.put(Treinos.ABDOMINAL, Treinos.ABDOMINAL);
	    mProjectionTreino.put(Treinos.AEROBICO, Treinos.AEROBICO);
	    mProjectionTreino.put(Treinos.PEITORAIS, Treinos.PEITORAIS);
	    mProjectionTreino.put(Treinos.COSTAS, Treinos.COSTAS);
	    mProjectionTreino.put(Treinos.OMBROS, Treinos.OMBROS);
	    mProjectionTreino.put(Treinos.TRICEPS, Treinos.TRICEPS);
	    mProjectionTreino.put(Treinos.BICEPS, Treinos.BICEPS);    
	    mProjectionTreino.put(Treinos.ANT_COXA, Treinos.ANT_COXA);
	    mProjectionTreino.put(Treinos.POST_COXA, Treinos.POST_COXA);
	    mProjectionTreino.put(Treinos.GLUTEOS, Treinos.GLUTEOS);
	    mProjectionTreino.put(Treinos.ADUTORES, Treinos.ADUTORES);
	    mProjectionTreino.put(Treinos.PANTURRILHA, Treinos.PANTURRILHA);
	    mProjectionAvaliacao.put(Avaliacoes.AVALIACAO_ID, Avaliacoes.AVALIACAO_ID);
	    mProjectionAvaliacao.put(Avaliacoes.DT_AVALIACAO, Avaliacoes.DT_AVALIACAO);
	    mProjectionAvaliacao.put(Avaliacoes.PESO, Avaliacoes.PESO);
	    mProjectionAvaliacao.put(Avaliacoes.PERC_GORDURA, Avaliacoes.PERC_GORDURA);	    
        mProjectionAvaliacao.put(Avaliacoes.PESCOCO, Avaliacoes.PESCOCO);
        mProjectionAvaliacao.put(Avaliacoes.BICEPS_DIREITO, Avaliacoes.BICEPS_DIREITO);
        mProjectionAvaliacao.put(Avaliacoes.BICEPS_ESQUERDO, Avaliacoes.BICEPS_ESQUERDO);
        mProjectionAvaliacao.put(Avaliacoes.ANTEBRACO_DIREITO, Avaliacoes.ANTEBRACO_DIREITO);
        mProjectionAvaliacao.put(Avaliacoes.ANTEBRACO_ESQUERDO, Avaliacoes.ANTEBRACO_ESQUERDO);
        mProjectionAvaliacao.put(Avaliacoes.PEITORAIS, Avaliacoes.PEITORAIS);
        mProjectionAvaliacao.put(Avaliacoes.CINTURA, Avaliacoes.CINTURA);
        mProjectionAvaliacao.put(Avaliacoes.QUADRIS, Avaliacoes.QUADRIS);
        mProjectionAvaliacao.put(Avaliacoes.COXA_DIREITA, Avaliacoes.COXA_DIREITA);
        mProjectionAvaliacao.put(Avaliacoes.COXA_ESQUERDA, Avaliacoes.COXA_ESQUERDA);
        mProjectionAvaliacao.put(Avaliacoes.PANTURRILHA_DIREITA, Avaliacoes.PANTURRILHA_DIREITA);
        mProjectionAvaliacao.put(Avaliacoes.PANTURRILHA_ESQUERDA, Avaliacoes.PANTURRILHA_ESQUERDA);    
	    mProjectionAvaliacao.put(Avaliacoes.NOME_AVALIADOR, Avaliacoes.NOME_AVALIADOR);
	    mProjectionAvaliacao.put(Avaliacoes.OBSERVACAO, Avaliacoes.OBSERVACAO);
	    mProjectionExercicios.put(Exercicios.EXERCICIO_ID, Exercicios.EXERCICIO_ID);	    
	    mProjectionExercicios.put(Exercicios.TIPO_EXERCICIO, Exercicios.TIPO_EXERCICIO);  
	    mProjectionExercicios.put(Exercicios.NOME_EXERCICIO, Exercicios.NOME_EXERCICIO);
	    mProjectionExercicios.put(Exercicios.EXCLUIDO, Exercicios.EXCLUIDO);
        
    }  
  
    static {  
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);  
        mMatcher.addURI(AUTHORITY, PROGRAMAS_TABLE, PROGRAMAS);  
        mMatcher.addURI(AUTHORITY, TREINOS_TABLE, TREINOS);  
        mMatcher.addURI(AUTHORITY, AVALIACOES_TABLE, AVALIACOES);  
        mMatcher.addURI(AUTHORITY, EXERCICIOS_TABLE, EXERCICIOS);  
    }  
  
    /////////////////////////////////////////////////////////////////  
    //           Métodos overrided de ContentProvider              //  
    /////////////////////////////////////////////////////////////////  
    @Override  
    public int delete(Uri uri, String selection, String[] selectionArgs) {  
        SQLiteDatabase db = mHelper.getWritableDatabase();  
        int count;  
        switch (mMatcher.match(uri)) {  
            case PROGRAMAS:  
                count = db.delete(PROGRAMAS_TABLE, selection, selectionArgs);  
                break;
            case TREINOS:  
                count = db.delete(TREINOS_TABLE, selection, selectionArgs);  
                break;
            case AVALIACOES:  
                count = db.delete(AVALIACOES_TABLE, selection, selectionArgs);  
                break;  
            case EXERCICIOS:  
                count = db.delete(EXERCICIOS_TABLE, selection, selectionArgs);  
                break;  
            default:  
                throw new IllegalArgumentException(  
                  "URI desconhecida " + uri);  
        }  
  
        getContext().getContentResolver().notifyChange(uri, null);  
        return count;  
    }  
  
    @Override  
    public String getType(Uri uri) {  
            switch (mMatcher.match(uri)) {  
                case PROGRAMAS:  
                    return Programas.CONTENT_TYPE;
                case TREINOS:  
                    return Treinos.CONTENT_TYPE;  
                case AVALIACOES:  
                    return Avaliacoes.CONTENT_TYPE;  
                case EXERCICIOS:  
                    return Avaliacoes.CONTENT_TYPE;  
                default:  
                    throw new IllegalArgumentException(  
                        "URI desconhecida " + uri);  
            }  
    }  
  
    @Override  
    public Uri insert(Uri uri, ContentValues values) {
    	long rowId;
    	SQLiteDatabase db;
        switch (mMatcher.match(uri)) {  
            case PROGRAMAS:  
                db = mHelper.getWritableDatabase();  
                rowId = db.insert(PROGRAMAS_TABLE, Programas.OBSERVACAO, values);  
                if (rowId > 0) {  
                    Uri programaUri = ContentUris.withAppendedId(  
                                 Programas.CONTENT_URI, rowId);  
                    getContext().getContentResolver().notifyChange(  
                                 programaUri, null);  
                    return programaUri;  
                }
            case TREINOS:  
                db = mHelper.getWritableDatabase();  
                rowId = db.insert(TREINOS_TABLE, Programas.OBSERVACAO, values);  
                if (rowId > 0) {  
                    Uri treinoUri = ContentUris.withAppendedId(  
                                 Treinos.CONTENT_URI, rowId);  
                    getContext().getContentResolver().notifyChange(  
                                 treinoUri, null);  
                    return treinoUri;  
                }  
            case AVALIACOES:  
                db = mHelper.getWritableDatabase();  
                rowId = db.insert(AVALIACOES_TABLE, Avaliacoes.DT_AVALIACAO, values);  
                if (rowId > 0) {  
                    Uri avaliacaoUri = ContentUris.withAppendedId(  
                                 Treinos.CONTENT_URI, rowId);  
                    getContext().getContentResolver().notifyChange(  
                    		avaliacaoUri, null);  
                    return avaliacaoUri;  
                }  
            case EXERCICIOS:  
                db = mHelper.getWritableDatabase();  
                rowId = db.insert(EXERCICIOS_TABLE, Exercicios.NOME_EXERCICIO, values);  
                if (rowId > 0) {  
                    Uri exercicioUri = ContentUris.withAppendedId(  
                                 Exercicios.CONTENT_URI, rowId);  
                    getContext().getContentResolver().notifyChange(  
                                 exercicioUri, null);  
                    return exercicioUri;  
                }  
                
            default:  
                throw new IllegalArgumentException(  
                        "URI desconhecida " + uri);  
        }  
    }  
  
    @Override  
    public boolean onCreate() {  
        mHelper = new DBHelper(getContext());;  
        return true;  
    }  
  
    @Override  
    public Cursor query(Uri uri, String[] projection, String selection,  
            String[] selectionArgs, String sortOrder) {  
            // Aqui usaremos o SQLiteQueryBuilder para construir  
            // a query que será feito ao DB, retornando um cursor  
            // que enviaremos à aplicação.  
            SQLiteQueryBuilder builder = new  SQLiteQueryBuilder();  
            SQLiteDatabase database = mHelper.getReadableDatabase();  
            Cursor cursor;  
            switch (mMatcher.match(uri)) {  
                case PROGRAMAS:  
                    // O Builer receberá dois parametros: a tabela  
                    // onde será feita a busca, e uma projection -  
                    // que nada mais é que uma HashMap com os campos  
                    // que queremos recuperar do banco de dados.  
                    builder.setTables(PROGRAMAS_TABLE);  
                    builder.setProjectionMap(mProjectionPrograma);  
                    break;
                    
                case TREINOS:  
                    // O Builer receberá dois parametros: a tabela  
                    // onde será feita a busca, e uma projection -  
                    // que nada mais é que uma HashMap com os campos  
                    // que queremos recuperar do banco de dados.  
                    builder.setTables(TREINOS_TABLE);  
                    builder.setProjectionMap(mProjectionTreino);  
                    break;  
  
                case AVALIACOES:  
                    // O Builer receberá dois parametros: a tabela  
                    // onde será feita a busca, e uma projection -  
                    // que nada mais é que uma HashMap com os campos  
                    // que queremos recuperar do banco de dados.  
                    builder.setTables(AVALIACOES_TABLE);  
                    builder.setProjectionMap(mProjectionAvaliacao);  
                    break;
                    
                case EXERCICIOS:  
                    // O Builer receberá dois parametros: a tabela  
                    // onde será feita a busca, e uma projection -  
                    // que nada mais é que uma HashMap com os campos  
                    // que queremos recuperar do banco de dados.  
                    builder.setTables(EXERCICIOS_TABLE);  
                    builder.setProjectionMap(mProjectionExercicios);  
                    break;
                    
                default:  
                    throw new IllegalArgumentException(  
                          "URI desconhecida " + uri);  
            }  
  
            cursor = builder.query(database, projection, selection,  
             selectionArgs, null, null, sortOrder);  
  
            cursor.setNotificationUri(getContext().getContentResolver(), uri);  
            return cursor;  
    }  
  
    @Override  
    public int update(Uri uri, ContentValues values, String selection,  
            String[] selectionArgs) {  
            SQLiteDatabase db = mHelper.getWritableDatabase();  
            int count;  
            switch (mMatcher.match(uri)) {  
                case PROGRAMAS:  
                    count = db.update(PROGRAMAS_TABLE, values,  
                                                     selection, selectionArgs);  
                    break;
                    case TREINOS:  
                        count = db.update(TREINOS_TABLE, values,  
                                                         selection, selectionArgs);  
                        break;  
                    case AVALIACOES:  
                        count = db.update(AVALIACOES_TABLE, values,  
                                                         selection, selectionArgs);  
                        break;  
                    case EXERCICIOS:  
                        count = db.update(EXERCICIOS_TABLE, values,  
                                                         selection, selectionArgs);  
                        break;  
                default:  
                    throw new IllegalArgumentException(  
                            "URI desconhecida " + uri);  
            }  
  
            getContext().getContentResolver().notifyChange(uri, null);  
            return count;  
    }  
  
    /////////////////////////////////////////////////////////////////  
    //                Inner Classes utilitárias                    //  
    /////////////////////////////////////////////////////////////////  
    public static final class  Programas implements  BaseColumns {  
        public static final Uri CONTENT_URI = Uri.parse("content://"  
                    + ProgramasProvider.AUTHORITY + "/programas");  
  
        public static final String CONTENT_TYPE =  
                "vnd.android.cursor.dir/" + ProgramasProvider.AUTHORITY;  
  
        public static final String PROGRAMA_ID = "_id";  
        public static final String DT_INICIO = "dt_inicio";  
        public static final String DT_FIM = "dt_fim";  
        public static final String NUM_DIAS = "num_dias";  
        public static final String NUM_SERIES = "num_series";  
        public static final String NUM_REPETICOES = "num_repeticoes"; 
        public static final String FREQUENCIA = "frequencia";  
        public static final String PESO = "peso";  
        public static final String OBJETIVO = "objetivo";
        public static final String OBSERVACAO = "observacao";
	    public static final String AEROBICO = "aerobico";
	    public static final String PEITORAIS = "peitorais";
	    public static final String COSTAS = "costas";
	    public static final String OMBROS = "ombros";
	    public static final String TRICEPS = "triceps";
	    public static final String BICEPS = "biceps";    
	    public static final String ANT_COXA = "ant_coxa";
	    public static final String POST_COXA = "post_coxa";
	    public static final String GLUTEOS = "gluteos";
	    public static final String ADUTORES = "adutores";
	    public static final String PANTURRILHA = "panturrilha";
        public static final String ABDOMINAL = "abdominal";
	    public static final String exercProg[] = new String[] {AEROBICO, PEITORAIS, COSTAS, OMBROS, TRICEPS,
	    													   BICEPS, ANT_COXA, POST_COXA, GLUTEOS, ADUTORES,
	    													   PANTURRILHA,ABDOMINAL}; 

        
    }  
    public static final class  Treinos implements  BaseColumns {  
        public static final Uri CONTENT_URI = Uri.parse("content://"  
                    + ProgramasProvider.AUTHORITY + "/treinos");  
  
        public static final String CONTENT_TYPE =  
                "vnd.android.cursor.dir/" + ProgramasProvider.AUTHORITY;  
  
        public static final String TREINO_ID = "_id";
        public static final String PROGRAMA_ID = "programa_id";        
        public static final String DT_TREINO = "dt_treino";  
        public static final String NUM_SESSAO = "num_sessao";  
        public static final String NUM_TREINO = "num_treino";  
        public static final String OBSERVACAO = "observacao";
        public static final String ABDOMINAL = "abdominal";
	    public static final String AEROBICO = "aerobico";
	    public static final String PEITORAIS = "peitorais";
	    public static final String COSTAS = "costas";
	    public static final String OMBROS = "ombros";
	    public static final String TRICEPS = "triceps";
	    public static final String BICEPS = "biceps";    
	    public static final String ANT_COXA = "ant_coxa";
	    public static final String POST_COXA = "post_coxa";
	    public static final String GLUTEOS = "gluteos";
	    public static final String ADUTORES = "adutores";
	    public static final String PANTURRILHA = "panturrilha";
        
        
    }
    
    public static final class  Avaliacoes implements  BaseColumns {  
        public static final Uri CONTENT_URI = Uri.parse("content://"  
                    + ProgramasProvider.AUTHORITY + "/avaliacoes");  
  
        public static final String CONTENT_TYPE =  
                "vnd.android.cursor.dir/" + ProgramasProvider.AUTHORITY;  
	    
        public static final String AVALIACAO_ID = "_id";
        public static final String DT_AVALIACAO = "dt_avaliacao";  
        public static final String PESO = "peso";  
        public static final String PERC_GORDURA = "perc_gordura";
        public static final String PESCOCO = "pescoco";
        public static final String BICEPS_DIREITO = "biceps_dir";
        public static final String BICEPS_ESQUERDO = "biceps_esq";
        public static final String ANTEBRACO_DIREITO = "antebraco_dir";
        public static final String ANTEBRACO_ESQUERDO = "antebraco_esq";
        public static final String PEITORAIS = "peitorais";
        public static final String CINTURA = "cintura";
        public static final String QUADRIS = "quadris";
        public static final String COXA_DIREITA = "coxa_dir";
        public static final String COXA_ESQUERDA = "coxa_esq";
        public static final String PANTURRILHA_DIREITA = "panturrilha_dir";
        public static final String PANTURRILHA_ESQUERDA = "panturrilha_esq";
        public static final String NOME_AVALIADOR = "nome_avaliador";
        public static final String OBSERVACAO = "observacao";
        

    }
    
    public static final class  Exercicios implements  BaseColumns {  
        public static final Uri CONTENT_URI = Uri.parse("content://"  
                    + ProgramasProvider.AUTHORITY + "/exercicios");  
  
        public static final String CONTENT_TYPE =  
                "vnd.android.cursor.dir/" + ProgramasProvider.AUTHORITY;  
	    
        public static final String EXERCICIO_ID = "_id";
        public static final String TIPO_EXERCICIO = "tipo_exercicio";  
        public static final String NOME_EXERCICIO = "nome_exercicio";
        public static final String EXCLUIDO = "excluido";
        

    }  

    private static class DBHelper extends SQLiteOpenHelper {  
  
        DBHelper(Context context) {  
            super(context, DATABASE_NAME, null, DATABASE_VERSION);  
        }  
  
        /* O método onCreate é chamado quando o provider é executado pela 
         * primeira vez, e usado para criar as tabelas no database 
         */  
        @Override  
        public void onCreate(SQLiteDatabase db) {
        	CriarTablePrograma(db);
        	CriarTableTreino(db);
        	CriarTableAvaliacao(db);
        	CriarTableExercicios(db);
            
        }  
  
        /* O método onUpdate é invocado quando a versão do banco de dados 
         * muda. Assim, é usado para fazer adequações para a aplicação 
         * funcionar corretamente. 
         */  
        @Override  
        public void onUpgrade(SQLiteDatabase db,  
                                      int oldVersion, int newVersion) {  
        	if (oldVersion == 1 && newVersion == 2)
            	CriarTableAvaliacao(db);
        	if (oldVersion == 2 && newVersion == 3){
            	CriarTableAvaliacao(db);
            	CriarTableExercicios(db);
        	}
        	if (oldVersion == 1 && newVersion == 3){
        		CriarTableTreino(db);
              	CriarTableAvaliacao(db);
            	CriarTableExercicios(db);
        	}
        	
        	
        }
        private void CriarTablePrograma(SQLiteDatabase db){
            db.execSQL("CREATE TABLE " + PROGRAMAS_TABLE + " (" +  
                    Programas.PROGRAMA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +  
                    Programas.DT_INICIO + " DATE," +  
                    Programas.DT_FIM + " DATE," +  
                    Programas.NUM_DIAS + " INTEGER," +  
                    Programas.PESO + " INTEGER," +
                    Programas.NUM_SERIES + " INTEGER," +
                    Programas.NUM_REPETICOES + " INTEGER," +
                    Programas.FREQUENCIA + " LONGTEXT," +                    
                    Programas.OBJETIVO + " LONGTEXT," +
                    Programas.OBSERVACAO + " LONGTEXT," + 
                    Programas.ABDOMINAL + " LONGTEXT," +
    	    		Programas.AEROBICO + " LONGTEXT," +
    	    		Programas.PEITORAIS + " LONGTEXT," +
    	    		Programas.COSTAS + " LONGTEXT," +
    	    		Programas.OMBROS + " LONGTEXT," +
    	    		Programas.TRICEPS + " LONGTEXT," +
    	    		Programas.BICEPS + " LONGTEXT," +    
    	    		Programas.ANT_COXA + " LONGTEXT," +
    	    		Programas.POST_COXA + " LONGTEXT," +
    	    		Programas.GLUTEOS + " LONGTEXT," +
    	    		Programas.ADUTORES + " LONGTEXT," +
    	    		Programas.PANTURRILHA + " LONGTEXT" + ");");
        	
        }
        private void CriarTableTreino(SQLiteDatabase db){
            
            db.execSQL("CREATE TABLE " + TREINOS_TABLE + " (" +  
                    Treinos.TREINO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +  
                    Treinos.PROGRAMA_ID + " INTEGER," +  
                    Treinos.DT_TREINO + " DATE," +  
                    Treinos.NUM_SESSAO + " INTEGER," +  
                    Treinos.NUM_TREINO + " LONGTEXT," +
                    Treinos.OBSERVACAO + " LONGTEXT," + 
                    Treinos.ABDOMINAL + " LONGTEXT," +
    	    		Treinos.AEROBICO + " LONGTEXT," +
    	    		Treinos.PEITORAIS + " LONGTEXT," +
    	    		Treinos.COSTAS + " LONGTEXT," +
    	    		Treinos.OMBROS + " LONGTEXT," +
    	    		Treinos.TRICEPS + " LONGTEXT," +
    	    		Treinos.BICEPS + " LONGTEXT," +    
    	    		Treinos.ANT_COXA + " LONGTEXT," +
    	    		Treinos.POST_COXA + " LONGTEXT," +
    	    		Treinos.GLUTEOS + " LONGTEXT," +
    	    		Treinos.ADUTORES + " LONGTEXT," +
    	    		Treinos.PANTURRILHA + " LONGTEXT" + ");");  
        	
        }
        private void CriarTableAvaliacao(SQLiteDatabase db){
            
            db.execSQL("CREATE TABLE " + AVALIACOES_TABLE + " (" +  
                    Avaliacoes.AVALIACAO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +  
                    Avaliacoes.DT_AVALIACAO + " DATE," +  
                    Avaliacoes.PESO + " INTEGER," +  
                    Avaliacoes.PERC_GORDURA + " INTEGER," +
                    Avaliacoes.PESCOCO + " INTEGER," +
                    Avaliacoes.BICEPS_DIREITO + " INTEGER," +
                    Avaliacoes.BICEPS_ESQUERDO + " INTEGER," +
                    Avaliacoes.ANTEBRACO_DIREITO + " INTEGER," +
                    Avaliacoes.ANTEBRACO_ESQUERDO + " INTEGER," +
                    Avaliacoes.PEITORAIS + " INTEGER," +
                    Avaliacoes.CINTURA + " INTEGER," +
                    Avaliacoes.QUADRIS + " INTEGER," +
                    Avaliacoes.COXA_DIREITA + " INTEGER," +
                    Avaliacoes.COXA_ESQUERDA + " INTEGER," +
                    Avaliacoes.PANTURRILHA_DIREITA + " INTEGER," +
                    Avaliacoes.PANTURRILHA_ESQUERDA + " INTEGER," +                    
                    Avaliacoes.NOME_AVALIADOR + " LONGTEXT, " +  
                    Avaliacoes.OBSERVACAO + " LONGTEXT" +");");  
        	
        }
        private void CriarTableExercicios(SQLiteDatabase db){
            
            db.execSQL("CREATE TABLE " + EXERCICIOS_TABLE + " (" +  
                    Exercicios.EXERCICIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +  
                    Exercicios.TIPO_EXERCICIO + " INTEGER," +  
                    Exercicios.NOME_EXERCICIO + " LONGTEXT," + 
                    Exercicios.EXCLUIDO + " TEXT" + ");");
            Log.d("Provide", "INSERT INTO " + TREINOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)");  
            
                        
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{0,"Caminhada"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{0,"Corrida"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{0,"Bicicleta"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{0, "Eliptico"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{1, "Supino sentado"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{1, "Supino reto"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{1, "Supino inclinado"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{1, "Voador"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{1, "Cruxifixo"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{1, "Fly"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{1, "Cross over"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{2, "Puxador pela frente"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{2, "Puxador por trás"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{2, "Puxador fechado"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{2, "Remada baixa"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{2, "Remada unilateral"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{2, "Voador Dorsal"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{2, "Pull Down"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{3, "Elevação lateral"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{3, "Elevação Frontal"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{3, "Desenvolvimento halteres"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{3, "Remada em pé"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{3, "Encolhimento de ombros"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{3, "Smith trás"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{4, "Tríceps Pulley"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{4, "Tríceps Pulley Inverso"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{4, "Tríceps Testa"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{4, "Rosca Francesa"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{4, "Tríceps Corda"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{5, "Rosca Direta com Halteres"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{5, "Rosca Direta com Barra"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{5, "Rosca Scott no Banco"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{5, "Rosca Alernada"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{5, "Rosca Inversa"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{5, "Rosca Concentrada"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{5, "Rosca Punho"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{5, "Rosca Direta no Cross"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{6, "Extensão com Caneleira"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{6, "Cadeira Extensora"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{6, "Leg Press 45º"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{6, "Hack Machine"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{6, "Agachamento"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{6, "Leg Horizontal"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{7, "Mesa Flexora"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{7, "Cadeira Flexora"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{7, "Stiff"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{7, "Flexora em pé"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{8, "Cadeira Abdutora"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{8, "Glúteo na Máquina"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{8, "Abdução com Caneleira"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{8, "Glúteo com Caneleira"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{9, "Adução com Caneleira"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{9, "Cadeira Adutora"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{10, "Panturrilha Sentada"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{10, "Panturrilha em pé"});
            db.execSQL("INSERT INTO " + EXERCICIOS_TABLE + " (" + Exercicios.TIPO_EXERCICIO + ", "+ Exercicios.NOME_EXERCICIO + ") VALUES(?, ?)",
            		new Object[]{11, "Abdominal Reto"});
        	
        }
        
    }  

}
