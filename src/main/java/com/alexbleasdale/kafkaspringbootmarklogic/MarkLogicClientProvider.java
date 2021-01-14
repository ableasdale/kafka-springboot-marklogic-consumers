package com.alexbleasdale.kafkaspringbootmarklogic;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.extra.gson.GSONHandle;
import com.marklogic.client.io.JacksonHandle;
import com.marklogic.client.io.marker.DocumentMetadataWriteHandle;

public class MarkLogicClientProvider {

    /* Initialization on Demand holder idiom: - create the client only once (and on first use) */
    private static class LazyHolder {
        static final DatabaseClient INSTANCE = DatabaseClientFactory
                .newClient("localhost", 8000, "Test",
                        new DatabaseClientFactory.DigestAuthContext("admin", "admin"));
    }

    public static DatabaseClient getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static void writeXmlDocument(String uri, DocumentMetadataWriteHandle metadataHandle, String content) {
        MarkLogicClientProvider.getInstance().newXMLDocumentManager().writeAs(uri, metadataHandle, content);
    }

    public static void writeJsonDocument(String uri, DocumentMetadataWriteHandle metadataHandle, JacksonHandle content) {
        MarkLogicClientProvider.getInstance().newJSONDocumentManager().writeAs(uri, metadataHandle, content);
    }
}