package wsiiz.holker.bus;

import android.util.Log;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class XlsxParser {
    boolean start = false;
    boolean loop = true;
    Date startDate;
    Date endDate;
    Calendar tempDate = Calendar.getInstance();
    List<List<String>> column = new ArrayList<>(9);

    //Separate array for every station(column)
    ArrayList<String> one = new ArrayList<>();
    ArrayList<String> two = new ArrayList<>();
    ArrayList<String> three = new ArrayList<>();
    ArrayList<String> four = new ArrayList<>();
    ArrayList<String> five = new ArrayList<>();
    ArrayList<String> six = new ArrayList<>();
    ArrayList<String> seven = new ArrayList<>();
    ArrayList<String> eight = new ArrayList<>();
    ArrayList<String> nine = new ArrayList<>();


    //Parse file and return array with whole parsed data
    public List<List<String>> startParse(String path) {
        File myFile = new File(path);
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(myFile);
            Log.i("MyLog", "Fine");
        } catch (FileNotFoundException e) {
            Log.i("MyLog", "File is wrong!");
        }

        XSSFWorkbook myWorkBook = null;
        try {
            assert fis != null;
            myWorkBook = new XSSFWorkbook(fis);
            Log.i("MyLog", "Work fine!");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("MyLog", "work wrong!");
        }

        assert myWorkBook != null;
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);

        for (Row row : mySheet) {
            Iterator<Cell> cellIterator = row.cellIterator();
            if (loop) {
                function(cellIterator);
            } else {
                break;
            }
        }

        column.add(0, one);
        column.add(1, two);
        column.add(2, three);
        column.add(3, four);
        column.add(4, five);
        column.add(5, six);
        column.add(6, seven);
        column.add(7, eight);
        column.add(8, nine);

        return column;
    }


    //Convert minutes to readable version
    public String minuteCovert() {
        String minute = String.valueOf(tempDate.get(Calendar.MINUTE));
        if (Integer.parseInt(minute) < 10) {
            return "0" + minute;
        } else {
            return minute;
        }
    }


    //Return hour and minutes (more readable)
    public String pasteTime(Date date) {
        tempDate.setTime(date);
        return tempDate.get(Calendar.HOUR_OF_DAY) + ":" + (minuteCovert());
    }

    //Increase the date and return 'date' + one day
    public Date increaseDay(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Log.i("MyLog", calendar.getTime().toString());
            return calendar.getTime();
        } else {
            return null;
        }

    }

    //Compare year to segregate the block of dates
    public boolean compareYear(String date) {
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        if (date.substring(date.length() - 4, date.length()).equals(year)) {
            return true;
        } else {
            return false;
        }
    }

    private String getDay() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (day < 10) {
            return "0" + String.valueOf(day);
        } else {
            return String.valueOf(day);
        }
    }

    //Compare day to segregate the block of dates
    public boolean compareDay(String date) {
        return compareYear(date) && date.substring(8, 10).equals(getDay());
    }

    //function in loop that filters data and fills arrays
    public void function(Iterator<Cell> cellIterator) {
        while (cellIterator.hasNext()) {
            //Sat Nov 03 00:00:00 GMT+01:00 2018
            Cell cell = cellIterator.next();
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && compareDay(cell.getDateCellValue().toString())) {
                start = true;
                startDate = cell.getDateCellValue();
                System.out.println(startDate);
                endDate = increaseDay(startDate);
            } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC &&
                    start && cell.getDateCellValue().toString().equals(endDate.toString())) {
                start = false;
                loop = false;
            } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && start) {
                pasteTime(cell.getDateCellValue());
                Log.i("MyLog", pasteTime(cell.getDateCellValue()));
                switch (cell.getColumnIndex()) {
                    case 1:
                        Log.i("MyLog", pasteTime(cell.getDateCellValue()));
                        one.add(pasteTime(cell.getDateCellValue()));
                        break;
                    case 2:
                        Log.i("MyLog", pasteTime(cell.getDateCellValue()));
                        two.add(pasteTime(cell.getDateCellValue()));
                        break;
                    case 3:
                        Log.i("MyLog", pasteTime(cell.getDateCellValue()));
                        three.add(pasteTime(cell.getDateCellValue()));
                        break;
                    case 4:
                        Log.i("MyLog", pasteTime(cell.getDateCellValue()));
                        four.add(pasteTime(cell.getDateCellValue()));
                        break;
                    case 5:
                        Log.i("MyLog", pasteTime(cell.getDateCellValue()));
                        five.add(pasteTime(cell.getDateCellValue()));
                        break;
                    case 6:
                        Log.i("MyLog", pasteTime(cell.getDateCellValue()));
                        six.add(pasteTime(cell.getDateCellValue()));
                        break;
                    case 7:
                        Log.i("MyLog", pasteTime(cell.getDateCellValue()));
                        seven.add(pasteTime(cell.getDateCellValue()));
                        break;
                    case 8:
                        Log.i("MyLog", pasteTime(cell.getDateCellValue()));
                        eight.add(pasteTime(cell.getDateCellValue()));
                        break;
                    case 9:
                        Log.i("MyLog", pasteTime(cell.getDateCellValue()));
                        nine.add(pasteTime(cell.getDateCellValue()));
                        break;
                    default:
                        break;
                }

            }
        }
    }
}
