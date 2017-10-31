package cn.com.lanlyc.core.util;

public class UuidTransation {
	public String UuidTransIntid (String UUID)
	{
		int length = UUID.length();
		String str1 = "";
		for (int i=0;i<length;i++) {
			 char ch1 = UUID.charAt(i);
			 switch(ch1){
	            case 'a':
	            	str1+="10";
	                break;
	            case 'b':
	            	str1+="11";
	                break;
	            case 'c':
	            	str1+="12";
	                break;
	            case 'd':
	            	str1+="13";
	                break;
	            case 'e':
	            	str1+="14";
	                break;
	            case 'f':
	            	str1+="15";
	                break;
	            case 'g':
	            	str1+="16";
	                break;
	            case 'h':
	            	str1+="17";
	                break;
	            case 'i':
	            	str1+="18";
	                break;
	            case 'j':
	            	str1+="19";
	                break;
	            case 'k':
	            	str1+="20";
	                break;
	            case 'l':
	            	str1+="21";
	                break;
	            case 'm':
	            	str1+="22";
	                break;
	            case 'n':
	            	str1+="23";
	                break;
	            case 'o':
	            	str1+="24";
	                break;
	            case 'p':
	            	str1+="25";
	                break;
	            case 'q':
	            	str1+="26";
	                break;
	            case 'r':
	            	str1+="27";
	                break;
	            case 's':
	            	str1+="28";
	                break;
	            case 't':
	            	str1+="29";
	                break;
	            case 'u':
	            	str1+="30";
	                break;
	            case 'v':
	            	str1+="31";
	                break;
	            case 'w':
	            	str1+="32";
	                break;
	            case 'x':
	            	str1+="33";
	                break;
	            case 'y':
	            	str1+="34";
	                break;
	            case 'z':
	            	str1+="35";
	                break;
	            case 'A':
	            	str1+="40";
	                break;
	            case 'B':
	            	str1+="41";
	                break;
	            case 'C':
	            	str1+="42";
	                break;
	            case 'D':
	            	str1+="43";
	                break;
	            case 'E':
	            	str1+="44";
	                break;
	            case 'F':
	            	str1+="45";
	                break;
	            case 'G':
	            	str1+="46";
	                break;
	            case 'H':
	            	str1+="47";
	                break;
	            case 'I':
	            	str1+="48";
	                break;
	            case 'J':
	            	str1+="49";
	                break;
	            case 'K':
	            	str1+="50";
	                break;
	            case 'L':
	            	str1+="51";
	                break;
	            case 'M':
	            	str1+="52";
	                break;
	            case 'N':
	            	str1+="53";
	                break;
	            case 'O':
	            	str1+="54";
	                break;
	            case 'P':
	            	str1+="55";
	                break;
	            case 'Q':
	            	str1+="56";
	                break;
	            case 'R':
	            	str1+="57";
	                break;
	            case 'S':
	            	str1+="58";
	                break;
	            case 'T':
	            	str1+="59";
	                break;
	            case 'U':
	            	str1+="60";
	                break;
	            case 'V':
	            	str1+="61";
	                break;
	            case 'W':
	            	str1+="62";
	                break;
	            case 'X':
	            	str1+="63";
	                break;
	            case 'Y':
	            	str1+="64";
	                break;
	            case 'Z':
	            	str1+="65";
	                break;
	            case '0':
	            	str1+="00";
	                break;
	            case '1':
	            	str1+="01";
	                break;
	            case '2':
	            	str1+="02";
	                break;
	            case '3':
	            	str1+="03";
	                break;
	            case '4':
	            	str1+="04";
	                break;
	            case '5':
	            	str1+="05";
	                break;
	            case '6':
	            	str1+="06";
	                break;
	            case '7':
	            	str1+="07";
	                break;
	            case '8':
	            	str1+="08";
	                break;
	            case '9':
	            	str1+="09";
	                break;   
	        }
		}
		return str1;
	}
	
	public String IntidTransUuid (String Intid)
	{
		int length = Intid.length();
		String str1 = "";
		for (int i=0;i<length;i=i+2) {
			 String str2 = Intid.substring(i, i+2);
			 int a = Integer.parseInt(str2);
			 switch(a){
	            case 0:
	            	str1+="0";
	                break;
	            case 1:
	            	str1+="1";
	                break;
	            case 2:
	            	str1+="2";
	                break;
	            case 3:
	            	str1+="3";
	                break;
	            case 4:
	            	str1+="4";
	                break;
	            case 5:
	            	str1+="5";
	                break;
	            case 6:
	            	str1+="6";
	                break;
	            case 7:
	            	str1+="7";
	                break;
	            case 8:
	            	str1+="8";
	                break;
	            case 9:
	            	str1+="9";
	                break;
	            case 10:
	            	str1+="a";
	                break;
	            case 11:
	            	str1+="b";
	                break;
	            case 12:
	            	str1+="c";
	                break;
	            case 13:
	            	str1+="d";
	                break;
	            case 14:
	            	str1+="e";
	                break;
	            case 15:
	            	str1+="f";
	                break;
	            case 16:
	            	str1+="g";
	                break;
	            case 17:
	            	str1+="h";
	                break;
	            case 18:
	            	str1+="i";
	                break;
	            case 19:
	            	str1+="j";
	                break;
	            case 20:
	            	str1+="k";
	                break;
	            case 21:
	            	str1+="l";
	                break;
	            case 22:
	            	str1+="m";
	                break;
	            case 23:
	            	str1+="n";
	                break;
	            case 24:
	            	str1+="o";
	                break;
	            case 25:
	            	str1+="p";
	                break;
	            case 26:
	            	str1+="q";
	                break;
	            case 27:
	            	str1+="r";
	                break;
	            case 28:
	            	str1+="s";
	                break;
	            case 29:
	            	str1+="t";
	                break;
	            case 30:
	            	str1+="u";
	                break;
	            case 31:
	            	str1+="v";
	                break;
	            case 32:
	            	str1+="w";
	                break;
	            case 33:
	            	str1+="x";
	                break;
	            case 34:
	            	str1+="y";
	                break;
	            case 35:
	            	str1+="z";
	                break;
	            case 40:
	            	str1+="A";
	                break;
	            case 41:
	            	str1+="B";
	                break;
	            case 42:
	            	str1+="C";
	                break;
	            case 43:
	            	str1+="D";
	                break;
	            case 44:
	            	str1+="E";
	                break;
	            case 45:
	            	str1+="F";
	                break;
	            case 46:
	            	str1+="G";
	                break;
	            case 47:
	            	str1+="H";
	                break;
	            case 48:
	            	str1+="I";
	                break;
	            case 49:
	            	str1+="J";
	                break;
	            case 50:
	            	str1+="K";
	                break;
	            case 51:
	            	str1+="L";
	                break;
	            case 52:
	            	str1+="M";
	                break;
	            case 53:
	            	str1+="N";
	                break;
	            case 54:
	            	str1+="O";
	                break;
	            case 55:
	            	str1+="P";
	                break;
	            case 56:
	            	str1+="Q";
	                break;
	            case 57:
	            	str1+="R";
	                break;
	            case 58:
	            	str1+="S";
	                break;
	            case 59:
	            	str1+="T";
	                break;
	            case 60:
	            	str1+="U";
	                break;
	            case 61:
	            	str1+="V";
	                break;
	            case 62:
	            	str1+="W";
	                break;
	            case 63:
	            	str1+="X";
	                break;
	            case 64:
	            	str1+="Y";
	                break;
	            case 65:
	            	str1+="Z";
	                break;   
	        }
		}
		return str1;
	}
}
