package com.example.commonlibs.utils;


public class HexDump {
	
    private final static char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    
    public static String dumpHexString(byte[] array)
    {
        return dumpHexString(array, 0, array.length);
    }
    
    public static String dumpHexString(byte[] array, int offset, int length)
    {
        StringBuilder result = new StringBuilder();
        
        byte[] line = new byte[16];
        int lineIndex = 0;
        
        result.append("\n0x");
        result.append(toHexString(offset));
        
        for (int i = offset ; i < offset + length ; i++)
        {
            if (lineIndex == 16)
            {
                result.append(" ");
                
                for (int j = 0 ; j < 16 ; j++)
                {
                    if (line[j] > ' ' && line[j] < '~')
                    {
                        result.append(new String(line, j, 1));
                    }
                    else
                    {
                        result.append(".");
                    }
                }
                
                result.append("\n0x");
                result.append(toHexString(i));
                lineIndex = 0;
            }
            
            byte b = array[i];
            result.append(" ");
            result.append(HEX_DIGITS[(b >>> 4) & 0x0F]);
            result.append(HEX_DIGITS[b & 0x0F]);
            
            line[lineIndex++] = b;
        }
        
        if (lineIndex != 16)
        {
            int count = (16 - lineIndex) * 3;
            count++;
            for (int i = 0 ; i < count ; i++)
            {
                result.append(" ");
            }
            
            for (int i = 0 ; i < lineIndex ; i++)
            {
                if (line[i] > ' ' && line[i] < '~')
                {
                    result.append(new String(line, i, 1));
                }
                else
                {
                    result.append(".");
                }
            }
        }
        
        return result.toString();
    }
    
    public static String toHexString(byte b)
    {
        return toHexString(toByteArray(b));
    }

    public static String toHexString(byte[] array)
    {
        return toHexString(array, 0, array.length);
    }
    
    public static String toHexString(byte[] array, int offset, int length)
    {
        char[] buf = new char[length * 2];

        int bufIndex = 0;
        for (int i = offset ; i < offset + length; i++) 
        {
            byte b = array[i];
            buf[bufIndex++] = HEX_DIGITS[(b >>> 4) & 0x0F];
            buf[bufIndex++] = HEX_DIGITS[b & 0x0F];
        }

        return new String(buf);        
    }
    
    public static String toHexString(int i)
    {
        return toHexString(toByteArray(i));
    }
    
    public static byte[] toByteArray(byte b)
    {
        byte[] array = new byte[1];
        array[0] = b;
        return array;
    }
    
    public static byte[] toByteArray(int i)
    {
        byte[] array = new byte[4];
        
        array[3] = (byte)(i & 0xFF);
        array[2] = (byte)((i >> 8) & 0xFF);
        array[1] = (byte)((i >> 16) & 0xFF);
        array[0] = (byte)((i >> 24) & 0xFF);
        
        return array;
    }

    
    public static byte[] hexStringToByteArray(String hexString)
    {
        int length = hexString.length();
        byte[] buffer = new byte[length / 2];

        for (int i = 0 ; i < length ; i += 2)
        {
            buffer[i / 2] = (byte)((toByte(hexString.charAt(i)) << 4) | toByte(hexString.charAt(i+1)));
        }
        
        return buffer;
    }


    /**
     * 字节数组转HEXDECIMAL
     *
     * @param bArray
     * @return
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }



    /**
     * 十六进制转换字符串
     *
     * @param String str Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }


    /**
     * @throws
     * @Title: hexStringToByte
     * @Description: 将16进制字符串转换成字节数组
     * @param: @param hex
     * @param: @return
     * @return: byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * @throws
     * @Title: toByte
     * @Description: 返回字符c首次出现的位置，并转化为字节类型
     * @param: @param c
     * @param: @return
     * @return: byte
     */
    public static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }
    
}