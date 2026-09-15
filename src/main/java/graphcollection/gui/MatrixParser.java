/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 *
 * @author Рома
 */
public class MatrixParser {

    public double[][] read(String fileName) {
        double[][] matrix = null;
        try (FileInputStream in = new FileInputStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, "Cp1251"))) {
            String line = reader.readLine();
            int n = Integer.parseInt(line);
            checkSize(n);
            matrix = createMatrix(n);
            StringBuilder str = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                checkLine(line, n);
                str.append(" ").append(line);
            }
            //----------------------------------------------
            StringTokenizer tokenizer = new StringTokenizer(str.toString());
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = Double.parseDouble(tokenizer.nextToken());
                }
            }
            //---------------------------------------------
        } catch (IOException e) {
            throw new InternalError(e);
        } catch (Exception e) {
            throw new NumberFormatException("Ошибка в задании матрицы!");
        }

        return matrix;
    }

    private double[][] createMatrix(int n) {
        return new double[n][n];
    }

    private void checkSize(int n) {
        if (n <= 0) {
            throw new NumberFormatException("Размерность матрицы должна быть положительной!");
        }
    }

    private void checkLine(String str, int n) {
        StringTokenizer tokenizer = new StringTokenizer(str);
        if (tokenizer.countTokens() != n) {
            throw new NumberFormatException();
        }
    }
}
