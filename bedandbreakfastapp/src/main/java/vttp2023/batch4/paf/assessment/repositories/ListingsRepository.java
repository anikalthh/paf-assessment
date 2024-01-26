package vttp2023.batch4.paf.assessment.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.bcel.Const;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.Utils;
import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.AccommodationSummary;

@Repository
public class ListingsRepository {
	
	// You may add additional dependency injections

	@Autowired
	private MongoTemplate template;

	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * //task 3 -- distinct suburbs
	 * db.listings.aggregate(
			{
				$group: {
					_id: "$address.suburb"
				}
			}
		);
	 *
	 */
	public List<String> getSuburbs(String country) {
		GroupOperation groupOps = Aggregation.group(Constants.L_ADDRESS_SUBURB);
		Aggregation pipeline = Aggregation.newAggregation(groupOps);
		AggregationResults<Document> docs = template.aggregate(pipeline, Constants.LISTINGS_COLLECTION, Document.class);

		List<String> listOfSuburbs = new LinkedList<>();
		for (Document d: docs.getMappedResults()) {
			listOfSuburbs.add(d.getString("_id"));
		}

		return listOfSuburbs;
	}

	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * db.listings.aggregate([
			{
				$match: {
					"address.suburb": { $regex: "?", $options: "i"},
					accommodates: { $gte: ? },
					price: { $gte: 50, $lte: ? },
					min_nights: { $gte: ? }
				}
			},
			{
				$project: {
					_id: 1, name: 1, accommodates: 1, price: 1
				}
			},
			{
				$sort: { price: -1 }
			}
		]);
	 *
	 *
	 */
	public List<AccommodationSummary> findListings(String suburb, int persons, int duration, float priceRange) {
		MatchOperation matchOps = Aggregation.match(
			Criteria.where(Constants.L_ADDRESS_SUBURB).is(suburb)
				.and(Constants.L_ACCOMMODATES).gte(persons)
				.and(Constants.L_PRICE).gte(50f).lte(priceRange)
				.and(Constants.L_MIN_NIGHTS).gte(duration)
		);

		ProjectionOperation projectOps = Aggregation.project(Constants.L_ID, Constants.L_NAME, Constants.L_ACCOMMODATES, Constants.L_PRICE);

		SortOperation sortOps = Aggregation.sort(Sort.by(Direction.DESC, Constants.L_PRICE));

		Aggregation pipeline = Aggregation.newAggregation(matchOps, projectOps, sortOps);

		AggregationResults<Document> results = template.aggregate(pipeline, Constants.LISTINGS_COLLECTION, Document.class);

		List<Document> docs = results.getMappedResults();

		List<AccommodationSummary> listOfSummaries = new LinkedList<>();
		for (Document d : docs) {
			listOfSummaries.add(Utils.AccSummaryfromDocument(d));
		}
		
		return listOfSummaries;
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public Optional<Accommodation> findAccommodatationById(String id) {
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = Query.query(criteria);

		List<Document> result = template.find(query, Document.class, "listings");
		if (result.size() <= 0)
			return Optional.empty();

		return Optional.of(Utils.toAccommodation(result.getFirst()));
	}

}
