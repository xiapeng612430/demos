package streamDemo;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import streamDemo.bean.Dish;
import streamDemo.bean.Dish.CaloricLevel;

public class StreamDemo {

    public static void main(String[] args) {
        List<Dish> menu = Arrays.asList(
            new Dish("pork", false, 800, Dish.Type.MEAT),
            new Dish("beef", false, 700, Dish.Type.MEAT),
            new Dish("chicken", false, 400, Dish.Type.MEAT),
            new Dish("french fries", true, 530, Dish.Type.OTHER),
            new Dish("rice", true, 350, Dish.Type.OTHER),
            new Dish("season fruit", true, 120, Dish.Type.OTHER),
            new Dish("pizza", true, 550, Dish.Type.OTHER),
            new Dish("prawns", false, 300, Dish.Type.FISH),
            new Dish("salmon", false, 450, Dish.Type.FISH)
        );

        //
        List<String> threeHighCaloricDishNames = menu.stream()
            .filter(d -> d.getCalories() > 450)
            .map(Dish::getName)
            .limit(3)
            .collect(toList());

        System.out.println(threeHighCaloricDishNames);

        List<String> title = Arrays.asList("1", "s", "asd");
        Stream<String> s = title.stream();
        s.forEach(System.out::println);
        //s.forEach(System.out::println);

        List<Dish> dishes = menu.stream()
            .filter(dish -> dish.getCalories() > 300)
            .skip(2)
            .collect(toList());

        System.out.println(dishes);

        List<Integer> dishNameLengths = menu.stream()
            .map(Dish::getName)
            .map(String::length)
            .collect(toList());
        System.out.println(dishNameLengths);

        String[] words = {"hello", "world"};
        Stream<String> wordsStream = Arrays.stream(words);

        List<String> uniqueCharacters = title
            .stream()
            .map(w -> w.split(""))
            .flatMap(Arrays::stream)
            .distinct()
            .collect(toList());
        System.out.println(uniqueCharacters);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sqares = numbers
            .stream()
            .map(n -> n * n)
            .collect(toList());
        System.out.println(sqares);

        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(4, 5);

        List<int[]> pairs = numbers1
            .stream()
            .flatMap(i -> numbers2
                .stream()
                .filter(j -> (i + j) % 3 == 0)
                .map(j -> new int[]{i, j}))
            .collect(toList());

        for (int[] pair : pairs) {
            System.out.println("(" + pair[0] + "," + pair[1] + ")");
        }

        if (menu.stream().anyMatch(Dish::isVegetarian)) {
            System.out.println("has vegetarian");
        }

        boolean isHealthy = menu.stream().allMatch(dish -> dish.getCalories() < 1000);
        System.out.println(isHealthy);

        isHealthy = menu.stream().noneMatch(dish -> dish.getCalories() >= 1000);
        System.out.println(isHealthy);

        Optional<Dish> vegetarianDish = menu
            .stream()
            .filter(Dish::isVegetarian)
            .findAny();
        System.out.println(vegetarianDish);

        menu.stream()
            .filter(Dish::isVegetarian)
            .findAny()
            .ifPresent(d -> System.out.println(d.getName()));

        List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> fitstNumber = someNumbers
            .stream()
            .map(x -> x * x)
            .filter(x -> x % 3 == 0)
            .findFirst();
        System.out.println(fitstNumber);

        int sum = someNumbers
            .stream()
            .reduce(0, (a, b) -> a + b);

        int sum1 = someNumbers.stream().reduce(0, Integer::sum);
        Optional<Integer> sum2 = someNumbers.stream().reduce(Integer::sum);
        System.out.println(sum2);

        Optional<Integer> max = someNumbers.stream().reduce(Integer::max);
        System.out.println("max: " + max);

        Long count = menu.stream().count();
        System.out.println("count" + count);

        int calories = menu.stream()
            .mapToInt(Dish::getCalories)
            .sum();
        System.out.println("calories = " + calories);

        IntStream evenNumbers = IntStream.rangeClosed(1, 100)
            .filter(n -> n % 2 == 0);
        System.out.println(evenNumbers.count());

        IntStream evenNumbers2 = IntStream.range(1, 100)
            .filter(n -> n % 2 == 0);
        System.out.println(evenNumbers2.count());

//        Stream<int[]> pythagoreanTriples = IntStream.rangeClosed(1, 100).boxed()
//                .flatMap(a ->
//                        IntStream.rangeClosed(a, 100)
//                                .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
//                                .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)}));
        Stream<double[]> pythagoreanTriples = IntStream.rangeClosed(1, 100).boxed()
            .flatMap(a -> IntStream.rangeClosed(a, 100)
                .mapToObj(b -> new double[]{a, b, Math.sqrt(a * a + b * b)}))
            .filter(t -> Math.sqrt(t[0] * t[0] + t[1] * t[1]) % 1 == 0)
            .limit(5);

        pythagoreanTriples
            .forEach(t -> System.out.println(t[0] + "," + t[1] + "," + t[2]));

        Stream<String> stream = Stream.of("a", "b", "c", "d");
        stream.map(String::toUpperCase).forEach(System.out::println);

        Stream<String> emptyStream = Stream.empty();

        int[] number = {2, 34, 5, 32, 45, 96};
        int numberSum = Arrays.stream(number).sum();
        System.out.println(numberSum);

        long uniqueWord = 0;
        try (Stream<String> lines = Files
            .lines(Paths.get("src/streamDemo/TraderTest.java"), Charset.defaultCharset())) {
            uniqueWord = lines.flatMap(line -> Arrays.stream(line.split("")))
                .distinct()
                .count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("uniqueWord size : " + uniqueWord);

        Stream.iterate(0, n -> n + 2)
            .limit(10)
            .forEach(System.out::println);

        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
            .limit(10)
            .forEach(t -> System.out.println("(" + t[0] + "," + t[1] + ")"));

        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
            .limit(20)
            .map(t -> t[0])
            .forEach(System.out::println);

        Stream.generate(Math::random)
            .limit(5)
            .forEach(System.out::println);

        long howManyDishes = menu.stream().collect(Collectors.counting());
        System.out.println("howManyDishes " + howManyDishes);
        long howManyDishes1 = menu.stream().count();
        System.out.println("howManyDishes " + howManyDishes1);

        Comparator<Dish> dishCaloriedComparator = Comparator.comparingInt(Dish::getCalories);
        Optional<Dish> mostCalorieDish = menu.stream()
            .collect(maxBy(dishCaloriedComparator));
        System.out.println("mostCalorieDish " + mostCalorieDish.toString());

        int totalCalories = menu.stream()
            .collect(Collectors.summingInt(Dish::getCalories));
        System.out.println("totalCalories: " + totalCalories);

        Double avgCalories = menu.stream()
            .collect(Collectors.averagingInt(Dish::getCalories));
        System.out.println("avgCalories " + avgCalories);

        IntSummaryStatistics summaryStatistics = menu.stream()
            .collect(Collectors.summarizingInt(Dish::getCalories));
        System.out.println(summaryStatistics);

        String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining(", "));
        System.out.println(shortMenu);

        int sum4 = someNumbers.parallelStream().reduce(0, Integer::sum);
        System.out.println("sum2 " + sum4);

        int totalCalories2 = menu.stream()
            .collect(reducing(0, Dish::getCalories, (i, j) -> i + j));
        System.out.println("totalCalories2 = " + totalCalories2);
        Optional<Dish> maxCaloriesDish = menu.stream()
            .collect(reducing((t1, t2) -> t1.getCalories() > t2.getCalories() ? t1 : t2));

        System.out.println(maxCaloriesDish);

        Map<Dish.Type, List<Dish>> dishedByType = menu.stream()
            .collect(groupingBy(Dish::getType));
        dishedByType
            .forEach((key, value) -> System.out.println("type: " + key + " value:" + value.toString()));

        Map<Dish.CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream()
            .collect(groupingBy(dishx -> {
                if (dishx.getCalories() <= 400) {
                    return CaloricLevel.DIET;
                } else if (dishx.getCalories() <= 700) {
                    return CaloricLevel.NORMAL;
                } else {
                    return CaloricLevel.FAT;
                }
            }));
        dishesByCaloricLevel.forEach((key, value) -> {
            System.out.println("CaloricLevel : " + key + " dishes: " + value);
        });

        Map<Dish.Type, Map<Dish.CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel = menu.stream()
            .collect(groupingBy(Dish::getType, groupingBy(dish -> {
                if (dish.getCalories() <= 400) {
                    return CaloricLevel.DIET;
                } else if (dish.getCalories() <= 700) {
                    return CaloricLevel.NORMAL;
                } else {
                    return CaloricLevel.FAT;
                }
            })));

        dishesByTypeCaloricLevel.forEach((key, value) -> {
            System.out.println("type : " + value.toString());
        });

        Map<Dish.Type, Long> typesCount = menu.stream()
            .collect(groupingBy(Dish::getType, counting()));
        System.out.println(typesCount);

        Map<Dish.Type, Dish> mostCaloricByType = menu.stream()
            // .collect(groupingBy(Dish::getType, maxBy(Comparator.comparing(Dish::getCalories))));
            .collect(groupingBy(Dish::getType,
                collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)));
        System.out.println(mostCaloricByType);

        Map<Dish.Type, Integer> totalCaloriesByType = menu.stream()
            .collect(groupingBy(Dish::getType, summingInt(Dish::getCalories)));
        System.out.println(totalCaloriesByType);

        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType = menu.stream()
            .collect(groupingBy(Dish::getType, mapping(dish ->
            {
                if (dish.getCalories() <= 400) {
                    return CaloricLevel.DIET;
                } else if (dish.getCalories() < 700) {
                    return CaloricLevel.NORMAL;
                } else {
                    return CaloricLevel.FAT;
                }
            }, toSet())));
        System.out.println(caloricLevelsByType);

        Map<Dish.Type, Set<CaloricLevel>> caloricLevelByType = menu.stream()
            .collect(
                groupingBy(
                    Dish::getType, mapping(dish -> {
                        if (dish.getCalories() <= 400) {
                            return CaloricLevel.DIET;
                        } else if (dish.getCalories() <= 700) {
                            return CaloricLevel.NORMAL;
                        } else {
                            return CaloricLevel.FAT;
                        }
                    }, toCollection(HashSet::new))
                )
            );
        System.out.println("caloricLevelByType: " + caloricLevelByType);

        Map<Boolean, List<Dish>> partitionedMenu = menu.stream()
            .collect(partitioningBy(Dish::isVegetarian));
        System.out.println(partitionedMenu);

        Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType =
            menu.stream()
                .collect(
                    partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType, toList()))
                );
        System.out.println(vegetarianDishesByType);

        Map<Boolean, Dish> mostCaloricPartitionedByVegetarian = menu.stream()
            .collect(partitioningBy(Dish::isVegetarian, collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)));
        System.out.println(mostCaloricPartitionedByVegetarian);

        Map<Boolean, List<Integer>> partitionPrimes = partitionPrimes(10);
        System.out.println(partitionPrimes);
    }

    public static boolean isPrime(int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return IntStream.range(2, candidateRoot)
            .noneMatch(i -> candidateRoot % i == 0);
    }

    public static Map<Boolean, List<Integer>> partitionPrimes(int n) {
        return IntStream.rangeClosed(2, n).boxed()
            .collect(partitioningBy(candidate -> isPrime(candidate)));
    }

}
