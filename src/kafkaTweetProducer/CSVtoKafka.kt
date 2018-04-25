package kafkaTweetProducer

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.google.gson.Gson
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import stormTweetAnalyzer.Twitter4JImpl.StatusImpl
import stormTweetAnalyzer.Twitter4JImpl.UserImpl
import twitter4j.Status
import utils.Constants
import java.io.File
import java.util.*
import java.util.concurrent.LinkedBlockingQueue


/**
 * Created by elson on 25/4/18.
 */
@Deprecated("Temporary")
object CSVtoKafka {

    @JvmStatic
    fun main(args: Array<String>) {
        val queue = LinkedBlockingQueue<Status>(1000)

        //Add Kafka producer config settings
        val props = Properties()
        props["metadata.broker.list"] = "localhost:9092"
        props["bootstrap.servers"] = "localhost:9092"
        props["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        props["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"

        val producer = KafkaProducer<String, String>(props)
        val gson = Gson()

        val mapper = CsvMapper()
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY)
        val csvFile = File("input.csv")
        val it = mapper.readerFor(Array<String>::class.java)
                .readValues<Array<String>>(csvFile)
        it.next()
        while (it.hasNext()) {
            val row = it.next()
            val user = UserImpl(row[1])
            //TODO: Fix this
            val date = Date(row[4])
            val status = StatusImpl(date, row[0],Integer.parseInt(row[3]),
                    Integer.parseInt(row[2]),user)
            producer.send(ProducerRecord(Constants.KAFKA_TOPIC, gson.toJson(status)))
        }

        producer.close()
    }
}