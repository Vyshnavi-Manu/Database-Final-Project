package DBProject;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RegretRatio {
    public static int memory_limit,ans_inp_size,total_size_allowed,data_measures,temp_inp_size;
    public static List<Float> temp_data_points,functions_data_points;

    public static double sampleSize(float epsilon, float delta) {
        return Math.floor(3 * Math.log(1.0 / delta) / (epsilon * epsilon));
    }

    public static void printLine(String s) {
        System.out.println(s);
    }

    public static DataPoint addNewDataPoint(int ind, DataPoint nextDataPoint) {
        return new DataPoint(ind,nextDataPoint);
    }

    public static void addSize(int size){
        memory_limit= memory_limit +  size;
    }

    public static int incrm(int var){
        return var+1;
    }
    public static DataPoint createDataPoint(int ind, DataPoint nextDataPoint) {
        addSize(DataPoint.SIZE);
        return new DataPoint(ind, nextDataPoint);
    }

    public static List<Float> getNumberList(String path) throws FileNotFoundException {
        List<Float> list;
        String number;

        list = new ArrayList<>();
        AtomicReference<Scanner> scanner = new AtomicReference<>(new Scanner(new File(path)));
        while (scanner.get().hasNext()) {
            number = scanner.get().next();
            list.add(Float.valueOf(number));
        }
        scanner.get().close();
        addSize(4 * list.size());
        return list;
    }

    public static float findSum(int util_ind, int raw_ind) {
        float sum;
        sum = 0;
        for (AtomicInteger i = new AtomicInteger(); i.get() < data_measures; i.getAndIncrement())
            sum = sum + functions_data_points.get(util_ind * data_measures + i.get()) * temp_data_points.get(raw_ind * data_measures + i.get());
        return sum;
    }

    public static void main(String[] args) throws IOException {


//        Initializations including enumerators and lists
        functions_data_points = new ArrayList<>();
        int satisfaction_point = 0;
        long time_started_at = System.nanoTime();
        ArrayList<Boolean> is_final_ans;
        int data_point_sample_size;
        DataPoint pointer = null;
        int result_size = 0;

        int minus_one = -1;
        int zero = 0;
        float minimum_value = 2;
        float total_regret_ratio = zero;
        double least_value = minus_one;
        int total_cases_avoided = zero;
        int users_avoided = zero;
        int user_cases = zero;
        int two = 2;
        int one = 1;
        int three = 3;

//      Reading the inputs
        FileWriter file_writer = new FileWriter("output.txt");
        PrintWriter file_printer = new PrintWriter(file_writer);

        System.out.println("#################");
        System.out.println("Given Inputs as");
        System.out.println(String.join(" ", args));
        System.out.println("#################");


        Object[] input_assigns = new Object[]{Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),Integer.parseInt(args[4])};
        String file = args[zero];
        ans_inp_size = (int)input_assigns[0];
        total_size_allowed = (int)input_assigns[1];
        data_measures = (int)input_assigns[2];
        temp_inp_size = (int)input_assigns[3];

        temp_data_points = getNumberList(file);

        is_final_ans = new ArrayList<>(total_size_allowed);
        addSize(4 * total_size_allowed);

        var best_datapoint_users = new ArrayList<DataPoint>(total_size_allowed);
        addSize(4 * total_size_allowed);

        int temp_pointer = zero;
        while (temp_pointer<total_size_allowed){
            is_final_ans.add(false);
            best_datapoint_users.add(null);
            temp_pointer++;
        }
        System.out.println("Dataset ROWS: " + total_size_allowed);
        System.out.println("Dataset Columns: " + data_measures);

        if (!(ans_inp_size < total_size_allowed)) {
            System.out.println("k >= SIZE(dataset) , should we continue? (y/n)");
            String user_input = new Scanner(System.in).next();
            System.out.println("Memory usage:" + memory_limit);

            switch (user_input) {

                case "y":
                    System.out.println("Exit!");
                    return;
            }
            ans_inp_size = total_size_allowed;
        }


        // Getting the inputs ready into the lists and ready for the algorithm to run
        data_point_sample_size = zero;

        data_point_sample_size = (temp_inp_size == 0)? (int) sampleSize(0.0001f, 0.1f): temp_inp_size;

        String[] printStatements= new String[]{"Sampling Size: " + data_point_sample_size,"k value: " + ans_inp_size,"#################"};
        System.out.println(printStatements[zero]);
        System.out.println(printStatements[one]);
        System.out.println(printStatements[two]);
        file_printer.println(String.format("k = %d, Sampling Size = %d, input = %s", ans_inp_size, temp_inp_size, file));

        addSize(4 * data_point_sample_size);

        float[] regular_data_point = new float[data_point_sample_size];
        addSize(4 * data_point_sample_size);
        float[] utility_data_point = new float[data_point_sample_size];
        addSize(4 * data_point_sample_size);


        satisfaction_point = minus_one;
        if (args.length == 6) {
            System.out.println("reading file: "+args[5]);
            functions_data_points = getNumberList(args[5]);
        }

        // Read utility samples and finding best points
        if (functions_data_points.isEmpty()) {
            int i1 = zero;
            while (i1 < data_point_sample_size * data_measures) {
                functions_data_points.add((float)Math.random());
                i1++;
            }
        }

        int k1 = 0;
        while (k1 < data_point_sample_size){
            float max = -1;
            int k2 = 0;
            while(k2<total_size_allowed){
                float ut;
                ut = findSum(k1,k2);
                if(ut>max){
                    max = ut;
                    satisfaction_point = k2;
                    regular_data_point[k1] = (ut>-1)?ut:regular_data_point[k1] ;
                    utility_data_point[k1] = (ut>-1)?ut:regular_data_point[k1];
                }
                k2++;
            }

            if (!is_final_ans.get(satisfaction_point)) {
                is_final_ans.set(satisfaction_point, true);
                result_size = result_size + 1;
                pointer = createDataPoint(satisfaction_point, pointer);
            }

            DataPoint best_data_point = best_datapoint_users.get(satisfaction_point);
            best_datapoint_users.set(satisfaction_point, createDataPoint(k1, best_data_point));

            k1++;
        }

        long time_taken = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - time_started_at);
        float user_time = time_taken/1000.0f;
        long system_time = time_taken;
        file_printer.println(String.format("User time : %f seconds", user_time));
        file_printer.println(String.format("System time : %d milliseconds\n", system_time));
        time_started_at = System.nanoTime();

        if (result_size <= ans_inp_size) {
            System.out.println("Average Regret Ratio is 0 so k is fewer for all the users!");
            file_printer.println("Average Regret Ratio is 0 so k is fewer for all the users!");
            file_printer.println("memoryUsage (bytes): " + memory_limit);
            file_printer.close();
            return;
        }



        System.out.println("Running Algorithm (Greedy Shrink) on Dataset");


        time_started_at = System.nanoTime();
        //Starting of the greedy shrink alogrithm on the nodes
        int m1 = result_size;
        while (m1> ans_inp_size){
            minimum_value = 2;
            int worst_data_point = -1;
            int iteration_count = 0;

            DataPoint worst_pointer = null;
            DataPoint worst_pointer_prev = null;
            DataPoint prev = null;

            DataPoint m_pointer1 = pointer;
            while (m_pointer1 != null){
                iteration_count = incrm(iteration_count);

                if (!(m_pointer1.val >= 0)) {
                    DataPoint new_pointer = m_pointer1;
                    Object[] assignValues = new Object[]{new_pointer.ind,new_pointer,prev};
                    worst_data_point = (int)assignValues[zero];
                    worst_pointer = (DataPoint) assignValues[one];
                    worst_pointer_prev = (DataPoint) assignValues[two];
                    break;
                }
                if(least_value !=-1){
                    if(m_pointer1.val > least_value) {
                        break;
                    }
                }
                AtomicInteger n = new AtomicInteger(m_pointer1.ind);
                int users_iterated = zero;
                float temp_sum_regret_ratio = total_regret_ratio;

                DataPoint users_pointer1 = best_datapoint_users.get(n.get());
                while(users_pointer1 != null){
                    DataPoint tempPointer2 = users_pointer1;
                    users_iterated = incrm(users_iterated);
                    int usrInd = tempPointer2.ind;
                    float calculated_value_partial = (regular_data_point[usrInd] - utility_data_point[usrInd]);
                    temp_sum_regret_ratio =   temp_sum_regret_ratio - (calculated_value_partial/ regular_data_point[usrInd]) ;

                    float maximum_score = minus_one;

                    DataPoint users_pointer2 = pointer;
                    while(users_pointer2 != null){
                        if (users_pointer2.ind != n.get()) {
                            float utility = findSum(usrInd, users_pointer2.ind);
                            if (utility > maximum_score) {
                                maximum_score = utility;
                            }
                        }
                        users_pointer2 = users_pointer2.nextDataPoint;
                    }
                    float calculated_value_partial2 = (regular_data_point[usrInd] - maximum_score);
                    temp_sum_regret_ratio = temp_sum_regret_ratio +  (calculated_value_partial2/ regular_data_point[usrInd]);

                    users_pointer1 = users_pointer1.nextDataPoint;
                }

                user_cases = incrm(user_cases);
                users_avoided += data_point_sample_size - users_iterated;
                float avg_regret_ratio = temp_sum_regret_ratio / data_point_sample_size;
                m_pointer1.val = avg_regret_ratio;

                if (avg_regret_ratio < minimum_value) {
                    Object[] temp_assign2 = new Object[]{ avg_regret_ratio,n.get(),m_pointer1,prev};
                    minimum_value = (float)temp_assign2[zero];
                    worst_data_point = (int)temp_assign2[one];
                    worst_pointer = (DataPoint) temp_assign2[two];
                    worst_pointer_prev = (DataPoint) temp_assign2[three];
                }
                prev = m_pointer1;
                m_pointer1 = m_pointer1.nextDataPoint;
            }

            total_cases_avoided += m1 - iteration_count;

            if (worst_pointer_prev != null) {
                worst_pointer_prev.nextDataPoint = worst_pointer.nextDataPoint;
            } else {
                assert worst_pointer != null;
                pointer = worst_pointer.nextDataPoint;
            }


            DataPoint usersIterator = best_datapoint_users.get(worst_data_point);
            while(usersIterator != null){
                Object[] tempAssign11 = new Object[]{usersIterator.ind};
                int usrIndex = (int)tempAssign11[zero];
                float tempCalculation11 = (regular_data_point[usrIndex] - utility_data_point[usrIndex]);
                total_regret_ratio = total_regret_ratio - (tempCalculation11  / regular_data_point[usrIndex]);
                satisfaction_point = minus_one;
                float maxUtility = minus_one;

                DataPoint iter2 = pointer;
                while(iter2 != null){
                    int j = iter2.ind;
                    float utility = findSum(usrIndex, j);
                    Object[] temp_assign3 = new Object[]{utility,j};
                    if (utility > maxUtility) {
                        maxUtility = (float)temp_assign3[zero];
                        satisfaction_point = (int)temp_assign3[one];
                        utility_data_point[usrIndex] = (float)temp_assign3[zero];
                    }
                    iter2 = iter2.nextDataPoint;
                }

                DataPoint bestDataPoint = best_datapoint_users.get(satisfaction_point);
                best_datapoint_users.set(satisfaction_point, addNewDataPoint(usrIndex, bestDataPoint));
                float calculation3 = (regular_data_point[usrIndex] - utility_data_point[usrIndex]) / regular_data_point[usrIndex];
                total_regret_ratio = total_regret_ratio+ calculation3;

                usersIterator = usersIterator.nextDataPoint;
            }

            best_datapoint_users.set(worst_data_point, null);

            DataPoint iter1 = pointer;
            while(iter1 != null){
                DataPoint iter2 = iter1;
                while(iter2 != null){
                    if (iter1.val > iter2.val) {
                        iter1.swapValue(iter2);
                        iter1.swapIndex(iter2);
                    }
                    iter2 = iter2.nextDataPoint;
                }
                iter1 = iter1.nextDataPoint;
            }


            DataPoint iter = pointer;
            DataPoint prev1 = null;
            int temp6 = zero;
            while (temp6<2){
                if(iter!=null){
                    prev1 = iter;
                    iter = iter.nextDataPoint;
                }
                else{
                    break;
                }
                temp6=incrm(temp6);
            }

            if (iter != null) {
                int start_index = iter.ind;
                float regret_ratio = total_regret_ratio;

                DataPoint it = best_datapoint_users.get(start_index);
                while(it != null){
                    float calculation10 = (regular_data_point[iter.ind] - utility_data_point[iter.ind]);
                    regret_ratio = regret_ratio - (calculation10/ regular_data_point[iter.ind]);

                    float maximum_utility = minus_one;

                    DataPoint iter2 = pointer;
                    while (iter2 != null){

                        int j = iter2.ind;
                        if (j != start_index){
                            float utility = findSum(iter.ind, j);
                            if (utility > maximum_utility) {
                                maximum_utility = utility;
                            }
                        }
                        iter2 = iter2.nextDataPoint;
                    }
                    regret_ratio += (regular_data_point[iter.ind] - maximum_utility) / regular_data_point[iter.ind];

                    it = it.nextDataPoint;
                }


                Object[] temp_assign10 = new Object[]{regret_ratio / data_point_sample_size};
                least_value = (float)temp_assign10[zero];
                iter.val = (float)temp_assign10[zero];
                prev1.nextDataPoint = iter.nextDataPoint;
                iter.nextDataPoint = pointer;
                pointer = iter;

                while(iter.nextDataPoint != null && iter.nextDataPoint.val < iter.val){

                    iter.swapValue();
                    iter.swapIndex();

                    iter = iter.nextDataPoint;
                }
            }


            m1--;
        }


        //found the best and worst data points and now applying the probability formulae on the best data points
        time_taken = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - time_started_at);
        float user_time10 = time_taken / 1000.0f;
        long system_time10 = time_taken;
        String[] print_statements2 = new String[]{"Time Taken for Algorithm: ",String.format("User time : %f seconds", user_time10),
                String.format("System time : %d milliseconds", system_time10)};
        file_printer.println(print_statements2[0]);
        file_printer.println(print_statements2[1]);
        file_printer.println(print_statements2[2]);
        file_printer.println("\n");

        System.out.println("Memory used in bytes :" + memory_limit);
        System.out.println("Average Regret Ratio: " + minimum_value);

        file_printer.println(String.format("Memory used in bytes: %d", memory_limit));
        file_printer.println("\n");

        //calculating more statistics
        if (functions_data_points.size() <= 0) {
            int p1 = 0;
            while(p1<data_point_sample_size * data_measures){
                functions_data_points.add((float)Math.random());
                p1 = incrm(p1);
            }
        }

        int p2 = 0;
        while(p2<data_point_sample_size){
            float maximum_utility = 0;
            int p3 = 0;
            while (p3<total_size_allowed){
                float utility = findSum(p2, p3);
                if (utility > maximum_utility) {
                    maximum_utility = utility;
                    regular_data_point[p2] = utility;
                }

                p3++;
            }
            p2++;
        }

        float sum_variance = 0;
        float min_regret_ratio = 0;
        List<Float> user_regret_ratios = new ArrayList<>();

        int user9 = 0;
        while(user9 < data_point_sample_size){

            float maximum_utilization = 0;
            DataPoint user9_pointer1 = pointer;
            while(user9_pointer1 != null){

                int i = user9_pointer1.ind;
                float utility = findSum(user9, i);
                if (utility > maximum_utilization) {
                    maximum_utilization = utility;
                }

                user9_pointer1 = user9_pointer1.nextDataPoint;
            }
            float[] temp_util = new float[]{maximum_utilization,regular_data_point[user9] - maximum_utilization};
            utility_data_point[user9] = temp_util[0];
            float calculation7 = temp_util[1];
            float regret_ratio = calculation7/ regular_data_point[user9];
            user_regret_ratios.add(regret_ratio);
            if (!(regret_ratio > min_regret_ratio)) {
            } else {
                min_regret_ratio = regret_ratio;
            }
            float partial_cal4  = (regret_ratio - minimum_value) * (regret_ratio - minimum_value);
            sum_variance = sum_variance + partial_cal4;

            user9 = incrm(user9);
        }

        float variance = sum_variance / data_point_sample_size;
        double std_dev = Math.sqrt(variance);
        int out_of_std_dev = 0;

        int user = 0;
        while(user < data_point_sample_size){
            float calculation9 = (regular_data_point[user] - utility_data_point[user]);
            float regret_ratio_temp =  calculation9/ regular_data_point[user];
            if (regret_ratio_temp > minimum_value + std_dev ) {
                out_of_std_dev = incrm(out_of_std_dev);
            }
            if(regret_ratio_temp < minimum_value - std_dev){
                out_of_std_dev = incrm(out_of_std_dev);
            }
            user = incrm(user);
        }

        Stream<Float> stream;
        stream = user_regret_ratios.stream();
        Stream<Float> sortedStream = stream.sorted();
        List<Float> sorted_users = sortedStream.collect(Collectors.toList());

        Object[] tempAssign9 = new Object[]{total_cases_avoided * 1.0 / (result_size - ans_inp_size),users_avoided * 1.0 / (user_cases)};
        String[] fileStrings = new String[]{String.format("Average Regret Ratio: %f", minimum_value),String.format("Sample sd: %f", std_dev),String.format("No within SD: %d", out_of_std_dev),String.format("Minimum Regret Ratio of the file is : %f", min_regret_ratio)};

        file_printer.println(fileStrings[zero]);
        file_printer.println(fileStrings[one]);
        file_printer.println(fileStrings[two]);

        //printing out the probability regret ratios.

        int[] percents = new int[]{70,80,90,99};
        for (int percent: percents) {
            float percentage = percent * 0.01f;
            file_printer.println(String.format(percent + "%% is : %f", sorted_users.get((int) (data_point_sample_size * percentage))));
        }
        file_printer.println(fileStrings[three]);
        file_printer.println(String.format("Iterations Avoided : %f", tempAssign9[zero]));
        file_printer.println(String.format("Users Avoided : %f", tempAssign9[one]));
        file_printer.println("\n");
        file_printer.close();
    }
}