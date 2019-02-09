import java.io.*;
import java.util.*;
import java.net.Socket;
/**
 *  there is a server. Try guess a 4-digit number that from 1 - 6
 *   api (int number) will return a "n m\n" that n: exact match count, m: correct but in wrong location
 *
 *   Solution: we try 5 times : 11111 - 55555 and get how many times of digit i in target number by receiving n
 *             then we add these digits into a set.
 *             For each digit x, we guess until we get correct location, by guessing xbbb, bxbb, bbxb and bbbx
 *             where b is outside the set
 *
 *             5 times for preTest
 *             4 + 3 + 2 = 9 times for first three digits
 *             the last one we don't need to guess
 *
 *
 */
public class GuessNumber {
    String target;

    public GuessNumber() {
        target = "";
    }
    public void generate() {
        Random rand = new Random();
        for (int i = 0; i < 4; i++) {
            target += rand.nextInt(6) + 1;
        }
    }
    public String guessNum() {
        //pre test to get count of each digit 1 - 6
        String[] test = new String[] {"", "1111", "2222", "3333", "4444", "5555"};
        int[] count = new int[7];
        int total = 0;
        for (int i = 1; i < 6; i++) {
            String ret = guess(test[i]);
            count[i] = ret.charAt(0) - '0';
            total += count[i];
        }
        count[6] = 4 - total;

        int[] target = new int[4];
        //find a nonexist digit in target, for guessing usage
        int nonexist = 0;
        for (int i = 1; i <= 6; i++) {
            if (count[i] == 0) {
                nonexist = i;
                break;
            }
        }

        //find position for each digit in target.
        for (int i = 1; i <= 6; i++) {
            for (int j = 0; j < count[i]; j++) {
                getPos(target, nonexist, i);
            }
        }
        String str = "";
        for (int i = 0; i < 4; i++) {
            str += target[i];
        }
        return str;
    }

    //get position of digit and store it in target array
    private void getPos(int[] target, int nonexist, int digit) {
        StringBuilder toGuess = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            toGuess.append(nonexist);
        }

        for (int i = 0; i < 3; i++) {
            if (target[i] == 0) {
                toGuess.setCharAt(i, (char) (digit + '0'));
                int res = guess(toGuess.toString()).charAt(0) - '0';
                if (res == 1) {
                    target[i] = digit;
                    return;
                }
            }
        }
        target[3] = digit;
    }


    String guess(String num) {
        int n = 0;
        int m = 0;
        int[] c1 = new int[7];
        int[] c2 = new int[7];
        for (int i = 0; i < 4; i++) {
            if (num.charAt(i) == target.charAt(i)) {
                n++;
            } else {
                c2[num.charAt(i) - '0']++;
                c1[target.charAt(i) - '0']++;
            }
        }
        for (int i = 1; i <= 6; i++) {
            m += Math.min(c1[i], c2[i]);
        }
        return n + " " + m + "/n";
    }

    public static void main(String[] args) {
        GuessNumber guessNumber = new GuessNumber();
        guessNumber.generate();
        System.out.println(guessNumber.target);
        System.out.println("we guess " + guessNumber.guessNum());

    }

    public class GuessClient {
        String hostName;
        int port;
        Socket socket;
        OutputStream outputStream;
        InputStream inputStream;
        PrintWriter writer;
        BufferedReader reader;

        public GuessClient (String hostName, int port) {
            this.hostName = hostName;
            this.port = port;
        }
        public void connect() throws IOException {
            socket = new Socket(hostName, port);
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            writer = new PrintWriter(outputStream, true);
            reader = new BufferedReader(new InputStreamReader(inputStream));

        }

        public String request(String message) {
            writer.print(message);
            try {
                return reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "ERROR";
        }

        public void close() {
            try {
                socket.close();
                outputStream.close();
                inputStream.close();
                writer.close();
                reader.close();
            } catch (Exception e) {
                System.out.println("Error!");
            }
        }
    }
}

