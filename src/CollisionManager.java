/*package src;

public class CollisionManager {

    public static boolean checkCollision(Box player, Attack attack) {
        // Your logic translated to Java variables:
        return attack.getX() + attack.getWidth() > player.getX() &&
               attack.getX() < player.getX() + player.getWidth() &&
               attack.getY() + attack.getHeight() > player.getY() &&
               attack.getY() < player.getY() + player.getHeight();
    }

    public static void handleCombat(Box character, Attack attack) {
        if (checkCollision(player, attack)) {
            // Apply damage using getters and setters
            int damageDealt = attack.getDamage();
            int newHp = player.getHp() - damageDealt;
            
            character.setHp(newHp);
            System.out.println("Hit! Player HP is now: " + character.getHp());
        }
    }
}
*/