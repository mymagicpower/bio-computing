package me.aias.service.impl;

import com.google.gson.JsonObject;
import io.milvus.client.*;
import lombok.extern.slf4j.Slf4j;
import me.aias.common.milvus.MilvusConnector;
import me.aias.domain.MolInfoDto;
import me.aias.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜素服务
 * Search service
 *
 * @author Calvin
 * @date 2021-12-19
 **/
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    @Value("${search.host}")
    String host;

    @Value("${search.port}")
    String port;                                    

    @Value("${search.dimension}")
    String dimension;

    @Value("${search.collectionName}")
    String collectionName;

    @Value("${search.indexFileSize}")
    String indexFileSize;

    @Value("${search.nprobe}")
    String nprobe;

    @Value("${search.nlist}")
    String nlist;

    @Autowired
    private MilvusConnector milvusConnector;

    public void initSearchEngine() {
        MilvusClient client = milvusConnector.getClient();
        HasCollectionResponse hasCollection = this.hasCollection(client, collectionName);
        if (hasCollection.hasCollection()) {
            this.dropCollection(client, collectionName);
            this.dropIndex(client, collectionName);
        }
    }

    public HasCollectionResponse hasCollection(MilvusClient client, String collectionName) {
        HasCollectionResponse response = client.hasCollection(collectionName);
        return response;
    }

    public HasCollectionResponse hasCollection(String collectionName) {
        MilvusClient client = milvusConnector.getClient();
        HasCollectionResponse response = client.hasCollection(collectionName);
        return response;
    }

    // 创建 collection
    // create Collection
    public Response createCollection(
            MilvusClient client, String collectionName, long dimension, long indexFileSize) {
        // 选择杰卡德距离 (Jaccard) 作为距离计算方式 MetricType.JACCARD
        // Choose Jaccard distance as the distance calculation method MetricType.JACCARD
        final MetricType metricType = MetricType.JACCARD;
        CollectionMapping collectionMapping =
                new CollectionMapping.Builder(collectionName, dimension)
                        .withIndexFileSize(indexFileSize)
                        .withMetricType(metricType)
                        .build();
        Response createCollectionResponse = client.createCollection(collectionMapping);
        return createCollectionResponse;
    }

    public Response createCollection(String collectionName, long dimension) {
        // 选择杰卡德距离 (Jaccard) 作为距离计算方式 MetricType.JACCARD
        // Choose Jaccard distance as the distance calculation method MetricType.JACCARD
        final MetricType metricType = MetricType.JACCARD;
        CollectionMapping collectionMapping =
                new CollectionMapping.Builder(collectionName, dimension)
                        .withIndexFileSize(Long.parseLong(indexFileSize))
                        .withMetricType(metricType)
                        .build();
        MilvusClient client = milvusConnector.getClient();
        Response createCollectionResponse = client.createCollection(collectionMapping);
        return createCollectionResponse;
    }

    // 删除 collection
    // Drop collection
    public Response dropCollection(MilvusClient client, String collectionName) {
        Response dropCollectionResponse = client.dropCollection(collectionName);
        return dropCollectionResponse;
    }

    // 查看 collection 信息
    // view collection info
    public Response getCollectionStats(MilvusClient client, String collectionName) {
        Response getCollectionStatsResponse = client.getCollectionStats(collectionName);
        //    if (getCollectionStatsResponse.ok()) {
        //      String jsonString = getCollectionStatsResponse.getMessage();
        //      System.out.format("Collection 信息: %s\n", jsonString);
        //    }
        return getCollectionStatsResponse;
    }

    // 插入向量
    // insert vectors
    public void insertVectors(String collectionName, List<MolInfoDto> list) {
        MilvusClient client = milvusConnector.getClient();
        List<Long> vectorIds = new ArrayList<>();
        List<ByteBuffer> vectors = new ArrayList<>();
        for (MolInfoDto textInfo : list) {
            vectorIds.add(textInfo.getId());
            vectors.add(textInfo.getFeature());
        }
        this.insertVectors(client, collectionName, vectorIds, vectors);
    }

    // 插入向量
    // insert vectors
    public void insertVectors(String collectionName, Long id, ByteBuffer feature) {
        MilvusClient client = milvusConnector.getClient();
        List<Long> vectorIds = new ArrayList<>();
        List<ByteBuffer> vectors = new ArrayList<>();
        vectorIds.add(id);
        vectors.add(feature);
        this.insertVectors(client, collectionName, vectorIds, vectors);
    }

    // 插入向量
    // insert vectors
    public void insertVectors(String collectionName, List<Long> vectorIds, List<ByteBuffer> vectors) {
        MilvusClient client = milvusConnector.getClient();
        this.insertVectors(client, collectionName, vectorIds, vectors);
    }

    public InsertResponse insertVectors(
            MilvusClient client, String collectionName, List<Long> vectorIds, List<ByteBuffer> vectors) {
        // 需要主动指定ID，如：图片的ID，用来关联图片资源，页面显示使用等
        // Active ID needs to be specified, for example: the ID of an image, which is used to associate with image resources and for displaying on the page.
        InsertParam insertParam =
                new InsertParam.Builder(collectionName)
                        .withVectorIds(vectorIds)
                        .withBinaryVectors(vectors)
                        .build();

        InsertResponse insertResponse = client.insert(insertParam);
        // 返回向量ID列表，向量ID如果不主动赋值，系统自动生成并返回
        // Return a list of vector IDs. If the vector ID is not assigned manually, the system will generate and return it automatically.
        //    List<Long> vectorIds = insertResponse.getVectorIds();
        return insertResponse;
    }

    // 刷新数据
    public Response flushData(MilvusClient client, String collectionName) {
        // Flush data in collection
        Response flushResponse = client.flush(collectionName);
        return flushResponse;
    }

    // 查询向量数量
    // Flush data in collection
    public long count(MilvusClient client, String collectionName) {
        // 获取数据条数
        // get number of vectors in collection
        CountEntitiesResponse ountEntitiesResponse = client.countEntities(collectionName);
        long rows = ountEntitiesResponse.getCollectionEntityCount();
        return rows;
    }

    // 搜索向量
    // search vectors
    public SearchResponse search(String collectionName, long topK, List<ByteBuffer> vectorsToSearch) {
        // 索引类型不同，参数也可能不同，查询文档选择最优参数
        // The parameter may vary depending on the index type. To achieve the best performance, select the optimal parameter when querying the document.
        JsonObject searchParamsJson = new JsonObject();
        searchParamsJson.addProperty("nprobe", Integer.parseInt(nprobe));
        SearchParam searchParam =
                new SearchParam.Builder(collectionName)
                        .withBinaryVectors(vectorsToSearch)
                        .withTopK(topK)
                        .withParamsInJson(searchParamsJson.toString())
                        .build();

        MilvusClient client = milvusConnector.getClient();
        SearchResponse searchResponse = client.search(searchParam);
        return searchResponse;
    }

    public SearchResponse search(
            MilvusClient client,
            String collectionName,
            int nprobe,
            long topK,
            List<ByteBuffer> vectorsToSearch) {

        // 索引类型不同，参数也可能不同，查询文档选择最优参数
        // Different index types may have different parameters. When querying documents, choose the optimal parameter for the index type.
        JsonObject searchParamsJson = new JsonObject();
        searchParamsJson.addProperty("nprobe", nprobe);
        SearchParam searchParam =
                new SearchParam.Builder(collectionName)
                        .withBinaryVectors(vectorsToSearch)
                        .withTopK(topK)
                        .withParamsInJson(searchParamsJson.toString())
                        .build();
        SearchResponse searchResponse = client.search(searchParam);
        return searchResponse;
    }

    // 创建 index
    // create index
    public Response createIndex(MilvusClient client, String collectionName) {
        // 索引类型在配置页面设置 IndexType.BIN_IVF_FLAT IVFLAT
        // Set the index type to IndexType.BIN_IVF_FLAT IVFLAT on the configuration page.
        final IndexType indexType = IndexType.IVFLAT;
        // 每种索引有自己的可选参数 - 在配置页面设置
        // Each index type has its own optional parameters, which can be set on the configuration page.
        JsonObject indexParamsJson = new JsonObject();
        indexParamsJson.addProperty("nlist", Integer.parseInt(nlist));
        Index index =
                new Index.Builder(collectionName, indexType)
                        .withParamsInJson(indexParamsJson.toString())
                        .build();

        Response createIndexResponse = client.createIndex(index);
        return createIndexResponse;
    }

    public Response createIndex(String collectionName) {
        // 索引类型在配置页面设置 IndexType.BIN_IVF_FLAT IVFLAT
        // Set the index type to IndexType.BIN_IVF_FLAT IVFLAT on the configuration page.
        final IndexType indexType = IndexType.IVFLAT;
        // 每种索引有自己的可选参数 - 在配置页面设置
        // Each index type has its own optional parameters, which can be set on the configuration page.
        JsonObject indexParamsJson = new JsonObject();
        indexParamsJson.addProperty("nlist", Integer.parseInt(nlist));
        Index index =
                new Index.Builder(collectionName, indexType)
                        .withParamsInJson(indexParamsJson.toString())
                        .build();
        MilvusClient client = milvusConnector.getClient();
        Response createIndexResponse = client.createIndex(index);
        return createIndexResponse;
    }

    // 查看索引信息
    // view index info
    public GetIndexInfoResponse getIndexInfo(MilvusClient client, String collectionName) {
        GetIndexInfoResponse getIndexInfoResponse = client.getIndexInfo(collectionName);
        // System.out.format("索引信息: %s\n",search.service.SearchServiceImpl.getIndexInfo(client,
        // collectionName).getIndex().toString());
        return getIndexInfoResponse;
    }

    // 删除 index
    // drop index
    public Response dropIndex(MilvusClient client, String collectionName) {
        Response dropIndexResponse = client.dropIndex(collectionName);
        return dropIndexResponse;
    }

    // 压缩 collection
    // compact collection
    public Response compactCollection(MilvusClient client, String collectionName) {
        // 压缩 collection, 从磁盘抹除删除的数据，并在后台重建索引（如果压缩后的数据比indexFileSize还要大）
        // Compress the collection to remove deleted data from the disk and rebuild the index in the background (if the compressed data is larger than the indexFileSize).
        // 在主动压缩前，数据只是软删除
        // Before actively compressing, the data is only soft deleted.
        Response compactResponse = client.compact(collectionName);
        return compactResponse;
    }

    //  检查 collection 中是否有 partition "tag"
    // check if partition exists in collection
    public HasPartitionResponse hasPartition(MilvusClient client, String collectionName, String tag) {
        HasPartitionResponse hasPartitionResponse = client.hasPartition(collectionName, tag);
        return hasPartitionResponse;
    }
}
