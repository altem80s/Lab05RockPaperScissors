
public class Random implements Strategy
{

    @Override
    public String getMove(String playerMove)
    {
        int choice = (int)(Math.random() * 3);

        if (choice == 0)
        {
            return "R";
        }
        else if (choice == 1)
        {
            return "P";
        }
        else
        {
            return "S";
        }
    }
}