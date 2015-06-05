package br.ufmt.lalg.compiler;

import br.ufmt.lalg.compiler.lexico.AnalisadorLexico;
import br.ufmt.lalg.compiler.lexico.AnaliseLexicaException;
import br.ufmt.lalg.compiler.sintatico.AnalisadorSintatico;
import br.ufmt.lalg.compiler.sintatico.AnaliseSintaticaException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {

    public static final String USAGE = "Usage: java -jar lalg-compiler file_name.lalg";

	public static void main(String[] args) {
		if(args.length != 1){
			System.out.println(USAGE);
            System.exit(-1);
		}

        String filename = args[0];
        if(!filename.endsWith(".lalg")){
            System.out.println("A .lalg file should be passed");
            System.exit(-1);
        }
        try {
            String conteudoArquivo = new String(Files.readAllBytes(Paths.get(filename)));

            AnalisadorLexico analisadorLexico = new AnalisadorLexico(conteudoArquivo);
            analisadorLexico.fazerAnalise();

            AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(analisadorLexico.tabela);
            analisadorSintatico.fazerAnalise();
            String codigoIntemediario = analisadorSintatico.getCodigoIntemediario();

            String filenameOut = filename.replace(".lalg",".blalg");
            Files.write(Paths.get(filenameOut),codigoIntemediario.getBytes());

        } catch (AnaliseLexicaException | AnaliseSintaticaException e ) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
