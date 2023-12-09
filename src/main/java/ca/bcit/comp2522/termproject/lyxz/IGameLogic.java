package ca.bcit.comp2522.termproject.lyxz;

public interface IGameLogic {
    void initializeData();
    void shuffleData();
    boolean isSolved();
    void moveTiles(Direction direction);
    boolean isMoveValid(Position targetPosition);

    int getTileValue(int i, int j);

}
