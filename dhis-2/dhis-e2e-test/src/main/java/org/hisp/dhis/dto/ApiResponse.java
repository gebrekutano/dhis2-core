package org.hisp.dhis.dto;

import com.google.gson.JsonObject;
import io.restassured.path.json.config.JsonParserType;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gintare Vilkelyte <vilkelyte.gintare@gmail.com>
 */
public class ApiResponse
{
    private Response raw;

    public ApiResponse( Response response )
    {
        raw = response;
    }

    /**
     * Extracts uid when only one object was created.
     *
     * @return
     */
    public String extractUid()
    {
        String uid;

        if ( extract( "response" ) == null )
        {
            return extractString( "id" );
        }

        uid = extractString( "response.uid" );

        if ( !StringUtils.isEmpty( uid ) )
        {
            return uid;
        }

        return extractString( "response.importSummaries.reference[0]" );
    }

    /**
     * Extracts uids from import summaries.
     * Use when more than one object was created.
     *
     * @return
     */
    public List<String> extractUids()
    {
        return extractList( "response.importSummaries.reference" );
    }

    public String extractString( String path )
    {
        return raw.jsonPath().getString( path );
    }

    public <T> T extractObject( String path, Class<T> type )
    {
        return raw.jsonPath()
            .getObject( path, type );
    }

    public Object extract( String path )
    {
        return raw.jsonPath().get( path );
    }

    public JsonObject extractJsonObject( String path )
    {
        return raw.jsonPath( JsonPathConfig.jsonPathConfig().defaultParserType( JsonParserType.GSON ) )
            .getObject( path, JsonObject.class );
    }

    public <T> List<T> extractList( String path )
    {
        return raw.jsonPath().getList( path );
    }

    public <T> List<T> extractList( String path, Class<T> type )
    {
        return raw.body().jsonPath().getList( path, type );
    }

    public int statusCode()
    {
        return raw.statusCode();
    }

    public ValidatableResponse validate()
    {
        return raw.then();
    }

    public JsonObject getBody()
    {
        return extractJsonObject( "" );
    }

    public boolean isEntityCreated()
    {
        return this.extractUid() != null;
    }

    public boolean containsImportSummaries()
    {
        return getContentType().contains( "json" ) ? !CollectionUtils.isEmpty( getImportSummaries() ) : false;
    }

    public List<ImportSummary> getImportSummaries()
    {
        String pathToImportSummaries = "";

        if ( this.extract( "response.responseType" ) != null )
        {
            pathToImportSummaries = "response.";
        }

        if ( this.extract( pathToImportSummaries + "responseType" ) != null )
        {
            switch ( this.extract( pathToImportSummaries + "responseType" ).toString() )
            {
            case "ImportSummaries":
                return this.extractList( pathToImportSummaries + "importSummaries", ImportSummary.class );
            case "ImportSummary":
                return Arrays.asList( this.raw.jsonPath().getObject( pathToImportSummaries, ImportSummary.class ) );
            }

        }

        return this.extractList( "response.importSummaries", ImportSummary.class );

    }

    public List<TypeReport> getTypeReports()
    {
        if ( this.extractList( "typeReports" ) != null )
        {
            return this.extractList( "typeReports", TypeReport.class );
        }

        return null;
    }

    public List<ImportSummary> getSuccessfulImportSummaries()
    {
        return getImportSummaries().stream()
            .filter( is -> {
                return is.getStatus().equalsIgnoreCase( "SUCCESS" );
            } )
            .collect( Collectors.toList() );
    }

    public String prettyPrint()
    {
        return raw.prettyPrint();
    }

    public String getAsString()
    {
        return raw.asString();
    }

    public String getContentType()
    {
        return raw.getContentType();
    }

}
