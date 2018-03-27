package com.dioxic.mgenerate.test;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.operator.Operator;
import org.assertj.core.util.Arrays;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OperatorTest {

	@Test
	public void array() {
		Object[] of = Arrays.array("fish", "bread", "turnip");
		int number = 4;

		Document args = new Document();
		args.put("of", of);
		args.put("number", number);

		Operator op = OperatorFactory.create("array", args);

		assertThat(op).as("null check").isNotNull();
		assertThat(op.resolve()).as("instance of array").isInstanceOf(List.class);
		assertThat((List)op.resolve()).as("has correct size").hasSize(number);
		assertThat((List)op.resolve()).as("is subset of expected values").isSubsetOf(of);

	}

	@Test
	public void choose() {
		String[] from = Arrays.array("fish", "bread", "turnip");
		Integer[] weights = Arrays.array(1,2,3);

		Document args = new Document();
		args.put("from", from);
		args.put("weights", weights);

		Operator op = OperatorFactory.create("choose", args);

		assertThat(op).as("null check").isNotNull();

		assertThat(op.resolve()).as("an expected value").isIn((Object[])from);
	}

	@Test
	public void arrayChoose() {
		String[] from = Arrays.array("fish", "bread", "turnip");

		Document numberArgs = new Document();
		numberArgs.put("min", 0);
		numberArgs.put("max", 5);

		Document arrayArgs = new Document();
		arrayArgs.put("of", OperatorFactory.create("choose", new Document("from", from)));
		arrayArgs.put("number", OperatorFactory.create("number", numberArgs));

		Operator op = OperatorFactory.create("array", arrayArgs);

		Object resolved = op.resolve();

		assertThat(op).as("null check").isNotNull();
		assertThat(resolved).as("instance of array").isInstanceOf(List.class);

		List resolvedArray = (List)op.resolve();

		assertThat(resolvedArray.size()).as("has correct size").isBetween(0, 5);
		assertThat(resolvedArray).as("is subset of expected values").isSubsetOf((Object[])from);
	}

	@Test
	public void objectId() {
		assertThat(OperatorFactory.create("objectid").resolve()).isInstanceOf(ObjectId.class);
	}
}
