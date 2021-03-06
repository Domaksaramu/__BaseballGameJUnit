import java.util.HashMap;
import java.util.Scanner;

public class BaseballGame {

    private Scanner scn = new Scanner(System.in);
    private int LEN = 3;

    public static void main(String[] args) {
        int regame = 1;
        BaseballGame baseballGame = new BaseballGame();
        baseballGame.startBaseball();

    }
    public BaseballGame() {
    }

    /**
     * 게임 시작 메소드
     * 첫 실행후 게임을 실행하며, 한 게임이 종료된 후엔 게임의 재시작, 종료 여부를 입력받아 처리한다.
     * @return 게임 종료 여부
     */
    public int startBaseball() {
        int regame_flag = 1;

        while (regame_flag != 2) {
            playOneGame(regame_flag);
            System.out.println("게임을 새로 시작하려면 1, 종료하려면 2를 입력하세요.");
            regame_flag = scn.nextInt();
        }

        return regame_flag;
    }

    /**
     * 한 게임을 실행하는 메소드
     * 3스트라이트가 나올 때까지 게임을 진행하며, 3스트라이크가 나오면 완료한다.
     * @param start_flag 게임 완료 여부
     */
    public void playOneGame(int start_flag){
        HashMap<Integer, Integer> target_numbers = null, inputed_numbers = null;
        String inputs;
        boolean res = false;
        int strike=0, ball=0;

        if(start_flag!=1){
            return;
        }

        target_numbers = this.initTargetNumbers();

        while(!res){
            System.out.print("숫자를 입력해주세요: ");
            inputs = scn.next();
            inputed_numbers = convertStringToNumbers(inputs);

            res = checkNumbers(target_numbers, inputed_numbers);

        }
    }

    /**
     * 경기의 종료 여부 판별
     * 입력 받은 숫자의 허용 여부(길이 3, 중복 불허) 및
     * 스트라이크, 볼 개수를 체크하여 경기의 종료 여부를 판별한다.
     * @param targets   대상 난수 Map
     * @param inputs    입력받은 숫자 Map
     * @return 게임 완료 여부
     */
    public boolean checkNumbers(HashMap<Integer,Integer> targets, HashMap<Integer,Integer> inputs) {
        boolean res = false;
        int strikes = 0;
        int balls = 0;
        if(inputs.size()!=LEN){
            System.out.println("입력한 숫자의 개수가 맞지 않거나 중복이 존재합니다. 다시 입력해주세요");
            return false;
        }
        for (int i = 1 ; i<10 ; i++){
            strikes += checkStrike(targets, inputs, i);
            balls += checkBall(targets, inputs, i);
        }

        printResult(strikes, balls);
        res = (strikes==3) && (balls == 0);

        return res;
    }

    /**
     * Strikes와 balls의 갯수에 따라 결과 출력
     * @param strikes   스트라이크 횟수
     * @param balls     볼 횟수
     */
    public void printResult(int strikes, int balls) {
        String res = new String();
        if(strikes>0){
            res += "" + strikes + "스트라이크 ";
        }
        if(balls>0){
            res += "" + balls + "볼 ";
        }
        if(strikes==0 && balls==0){
            res = "포볼";
        }
        if(strikes==3 && balls==0){
            res = "3개의 숫자를 모두 맞히셨습니다! 게임종료";
        }
        System.out.println(res);
    }

    /**
     * Strike 판정
     * 타겟 숫자맵과 입력받은 숫자맵에 해당 key(값)을 비교하여, 값이 양쪽 모두 존재하며 value(인덱스)가 같을 경우 strike로 판정한다.
     * @param targets   대상 난수 Map
     * @param inputs    입력받은 숫자 Map
     * @param num       비교 값
     * @return strike 여부
     */
    public int checkStrike(HashMap<Integer,Integer> targets, HashMap<Integer,Integer> inputs, int num) {
        int res=0;
        Integer target = targets.get(num);
        Integer input = inputs.get(num);
        if(target!=null && input!=null && target==input){
            res+=1;
        }
        return res;
    }

    /**
     * ball 판정
     * 타겟 숫자맵과 입력받은 숫자맵에 해당 key(값)을 비교하여, 값이 양쪽 모두 존재하며 value(인덱스)가 다를 경우 ball로 판정한다.
     * @param targets   대상 난수 Map
     * @param inputs    입력받은 숫자 Map
     * @param num       비교 값
     * @return ball 여부
     */
    public int checkBall(HashMap<Integer,Integer> targets, HashMap<Integer,Integer> inputs, int num) {
        int res=0;
        Integer target = targets.get(num);
        Integer input = inputs.get(num);
        if(target!=null && input!=null && target!=input){
            res+=1;
        }
        return res;
    }

    /**
     * getRandomNumber을 반복하여 타겟 숫자 HashMap을 생성
     * 중복되지 않는 난수를 추출하였으므로, 타겟 HashMap은 size 3 을 갖는다.
     * @return 타겟 숫자 HashMap
     */
    public HashMap<Integer, Integer> initTargetNumbers(){
        HashMap<Integer, Integer> res = new HashMap<>();
        int temp_number = 0;
        for(int i = 0; i<LEN; i++){
            temp_number = getRandomNumber(res);
            res.put(temp_number, i);
        }
        System.out.println("타겟 숫자 : " + res);
        return res;
    }

    /**
     * 한자릿수 난수 추출
     * 중복되는 숫자가 나올 경우, 재귀를 통해 다시 추출하도록 한다.
     * @param res 기존에 추출한 랜덤 숫자 HashMap
     * @return 한자리 난수
     */
    public int getRandomNumber(HashMap<Integer, Integer> res) {
        int rand_number = (int)(Math.random()*9+1);
        if(res.get(rand_number)!=null){
            rand_number = this.getRandomNumber(res);
        }
        return rand_number;
    }

    /**
     * 입력받은 String을 각 자리값과 인덱스를 갖는 HashMap으로 변환
     * 입력받은 숫자의 길이, 중복 여부는 해당 메소드에서 판별하지 않는다.
     * @param str 입력받은 숫자
     * @return res 자리값을 키, 인덱스를 값으로 갖는 HashMap
     */
    public HashMap<Integer, Integer> convertStringToNumbers(String str){
        HashMap<Integer, Integer> res = new HashMap<>();
        str = str.trim();
        for(int i = 0; i<str.length(); i++){
            res.put(str.charAt(i)-'0', i);
        }

        return res;
    }

}
