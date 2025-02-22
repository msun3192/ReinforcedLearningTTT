// Matthew Sun
// Mr. Paige
// Machine Learning
// 1/27/25
public enum
Transform {

    IDENTITY   (new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 }),
    ROTATE_90  (new int[] { 2, 5, 8, 1, 4, 7, 0, 3, 6 }),
    ROTATE_180 (new int[] { 8, 7, 6, 5, 4, 3, 2, 1, 0 }),
    ROTATE_270 (new int[] { 6, 3, 0, 7, 4, 1, 8, 5, 2 }),
    FLIP_HORZ  (new int[] { 2, 1, 0, 5, 4, 3, 8, 7, 6 }),
    FLIP_VERT  (new int[] { 6, 7, 8, 3, 4, 5, 0, 1, 2 }),
    ROTATE_90_THEN_FLIP_HORZ (new int[] { 0, 3, 6, 1, 4, 7, 2, 5, 8 }),
    ROTATE_90_THEN_FLIP_VERT (new int[] { 8, 5, 2, 7, 4, 1, 6, 3, 0 });

    private int[] mapping;

    private Transform(int[] mapping) {
        assert mapping.length == Board.N;
        this.mapping = mapping;
    }

    public Transform inverse() {
        switch(this) {
            case ROTATE_90:  return ROTATE_270;
            case ROTATE_270: return ROTATE_90;
            default:         return this;
        }
    }

    public int map(int index) {
        // Return the specified index on the transformed board
        return this.mapping[index];
    }

    public Board apply(Board board) {
        Board result = new Board();
        for (int i = 0; i < Board.N; i++) {
            result.set(this.mapping[i], board.get(i));
        }
        return result;
    }
}

