public class CharacterIcon {

    public static void Normal(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y + 1, x + 1, "O ");
        System.out.printf("\033[%d;%dH%s", y + 2, x, "/|\\");
        System.out.printf("\033[%d;%dH%s", y + 3, x, "/ \\");
    }

    public static void Paladin(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y, x, "O __");
        System.out.printf("\033[%d;%dH%s", y + 1, x - 6, "<==|-/|')_)");
        System.out.printf("\033[%d;%dH%s", y + 2, x - 1, "/ \\");
    }

    public static void Mage(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y, x, "_A_");
        System.out.printf("\033[%d;%dH%s", y + 1, x + 1, "O  *");
        System.out.printf("\033[%d;%dH%s", y + 2, x, "/|\\/");
        System.out.printf("\033[%d;%dH%s", y + 3, x, "/ \\");
    }

    public static void Warrior(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y, x, "(> O <)");
        System.out.printf("\033[%d;%dH%s", y + 1, x + 1, "|/|\\|");
        System.out.printf("\033[%d;%dH%s", y + 2, x + 1, "'/ \\'");
    }

    // Plains Mobs ------------------------------------------------

    public static void Slime(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y, x, "('' )");
    }

    public static void WildBull(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y, x, "<__>___   ");
        System.out.printf("\033[%d;%dH%s", y + 1, x, " \\/____)\\");
        System.out.printf("\033[%d;%dH%s", y + 2, x, "  ||  || ");
    }

    public static void Wolf(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y, x, "  ,,___/");
        System.out.printf("\033[%d;%dH%s", y + 1, x, "-'']___|");
        System.out.printf("\033[%d;%dH%s", y + 2, x, "  || ||");
    }

    public static void Minotaur(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y, x, " (__) <)");
        System.out.printf("\033[%d;%dH%s", y + 1, x, "  \\/,_|");
        System.out.printf("\033[%d;%dH%s", y + 2, x, " /(_) '");
        System.out.printf("\033[%d;%dH%s", y + 3, x, "  /'\\");
    }

    // -------------------------------------------------------------

    //Desert Mobs---------------------------------------------------

    public static void Mummy(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y + 1, x, " _{)");
        System.out.printf("\033[%d;%dH%s", y + 2, x, "',-)");
        System.out.printf("\033[%d;%dH%s", y + 3, x, "  /|");
    }

    public static void Spider(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y, x, "    __");
        System.out.printf("\033[%d;%dH%s", y + 1, x, "   (  )");
        System.out.printf("\033[%d;%dH%s", y + 2, x, " /'∞|'\\\\");
        System.out.printf("\033[%d;%dH%s", y + 3, x, "'   '  ''");
    }

    public static void Snake(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y, x, "    _");
        System.out.printf("\033[%d;%dH%s", y + 1, x, ">--( \\______");
        System.out.printf("\033[%d;%dH%s", y + 2, x, "    '-----, )");
        System.out.printf("\033[%d;%dH%s", y + 3, x, "          ;/");
    }

    public static void GiantWorm(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y, x, "   __");
        System.out.printf("\033[%d;%dH%s", y + 1, x, " ,'  ',       __");
        System.out.printf("\033[%d;%dH%s", y + 2, x, "(      \\    .'  ',");
        System.out.printf("\033[%d;%dH%s", y + 3, x, " ',_    '._/   _  \\");
        System.out.printf("\033[%d;%dH%s", y + 4, x, "     \\        / ', \\");
        System.out.printf("\033[%d;%dH%s", y + 5, x, "      '.____.'    \\|");
    }

    //---------------------------------------------------------------
    
    //LAVA WORLDDDDD heheheheh< johan

    public static void LavaImp(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y + 0, x + 8, "^-^");
        System.out.printf("\033[%d;%dH%s", y + 1, x + 6, "<(\"U\")>");
        System.out.printf("\033[%d;%dH%s", y + 2, x + 6, "/(   )\\");
        System.out.printf("\033[%d;%dH%s", y + 3, x + 8, "V V");
    }

    
    public static void MagmaBeast(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y + 0, x + 3, "(,---,)");
        System.out.printf("\033[%d;%dH%s", y + 1, x + 1, "(_/ ` ` \\_)");
        System.out.printf("\033[%d;%dH%s", y + 2, x + 3, "( v   v )");
    }

    
    public static void SkeletonHead(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y + 0, x + 4, ",-----,");
        System.out.printf("\033[%d;%dH%s", y + 1, x + 3, "( o   o )");
        System.out.printf("\033[%d;%dH%s", y + 2, x + 2, "~   )_ W _(  ~");
        System.out.printf("\033[%d;%dH%s", y + 3, x + 3, "~   HHH  ~");
        System.out.printf("\033[%d;%dH%s", y + 4, x + 5, "~  H  ~");
    }

    
    public static void Golem(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y + 0, x + 6, "(__[\"\"]__)");
        System.out.printf("\033[%d;%dH%s", y + 1, x + 7, "//[  ]\\\\");
        System.out.printf("\033[%d;%dH%s", y + 2, x + 6, "//  []  \\\\");
        System.out.printf("\033[%d;%dH%s", y + 3, x + 9, "//  \\\\");
    }
    
    //-----------------------------------------------------------------------

    //SNOWY ICELAND - faeana (nagkaprob in pushing)
    
    public static void GiantFrostWolves(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y + 0, x + 3, "\\_\\");
        System.out.printf("\033[%d;%dH%s", y + 1, x + 1, "._/* \\/\\/\\/\\_)");
        System.out.printf("\033[%d;%dH%s", y + 2, x + 0, "VV\\__        |");
        System.out.printf("\033[%d;%dH%s", y + 3, x + 6, "\\|____|/");
        System.out.printf("\033[%d;%dH%s", y + 4, x + 6, "]]    ]]");
    }
    
    public static void SnowGolem(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y + 0, x + 6, "(@_{**}_@)");
        System.out.printf("\033[%d;%dH%s", y + 1, x + 7, "//{  }\\\\");
        System.out.printf("\033[%d;%dH%s", y + 2, x + 6, "//  {}  \\\\");
        System.out.printf("\033[%d;%dH%s", y + 3, x + 8, "//  \\\\");
    }
    
    public static void WitchGnome(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y + 0, x + 6, "/\\");
        System.out.printf("\033[%d;%dH%s", y + 1, x + 4, "_/__\\__");
        System.out.printf("\033[%d;%dH%s", y + 2, x + 5, "*  {*)\\");
        System.out.printf("\033[%d;%dH%s", y + 3, x + 4, "\\/| \\ \\");
        System.out.printf("\033[%d;%dH%s", y + 4, x + 6, "]_]");
    }
 
    public static void Yeti(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y + 0, x + 4, "{*.* )");
        System.out.printf("\033[%d;%dH%s", y + 1, x + 3, "/)|)  )");
        System.out.printf("\033[%d;%dH%s", y + 2, x + 3, "/) |)  )");
        System.out.printf("\033[%d;%dH%s", y + 3, x + 2, "Cc  |) \\)");
        System.out.printf("\033[%d;%dH%s", y + 4, x + 5, "Cc  Cc");
    }
    
    //---------------------------------------------------------------

    //FINAL BOSS <johan ari lang nko ilagay para malahi
    
    public static void DemonLord(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y + 0, x + 15, "(_)");
        System.out.printf("\033[%d;%dH%s", y + 1, x + 9,  "_(\\/\\ <') /\\/)_");
        System.out.printf("\033[%d;%dH%s", y + 2, x + 7,  "_( -- ^^---^^ -- )_");
        System.out.printf("\033[%d;%dH%s", y + 3, x + 6,  "(_____/ /( )\\ \\_____)");
        System.out.printf("\033[%d;%dH%s", y + 4, x + 13, "|/ \\|");
    }

    //-chrisnel
    public static void Kyros(int x, int y) {
        System.out.printf("\033[%d;%dH%s", y + 0, x + 1, "/\\ /\\");
        System.out.printf("\033[%d;%dH%s", y + 1, x + 0, "( O.O )");
        System.out.printf("\033[%d;%dH%s", y + 2, x + 1, " \\_V_/ ");
        System.out.printf("\033[%d;%dH%s", y + 3, x + 0, " /(_)\\\\");
        System.out.printf("\033[%d;%dH%s", y + 4, x + 0, "/  ||  \\");
        System.out.printf("\033[%d;%dH%s", y + 5, x + 1, " \\_ _/");
    }

}


