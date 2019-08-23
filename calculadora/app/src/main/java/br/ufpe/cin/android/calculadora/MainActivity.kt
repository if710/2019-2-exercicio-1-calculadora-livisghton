package br.ufpe.cin.android.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_9.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_9.text.toString())
        }

        btn_8.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_8.text.toString())
        }

        btn_7.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_7.text.toString())
        }

        btn_6.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_6.text.toString())
        }

        btn_5.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_5.text.toString())
        }

        btn_4.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_4.text.toString())
        }

        btn_3.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_3.text.toString())
        }

        btn_2.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_2.text.toString())
        }

        btn_1.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_1.text.toString())
        }

        btn_0.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_0.text.toString())
        }

        btn_Dot.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_Dot.text.toString())
        }

        btn_LParen.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_LParen.text.toString())
        }

        btn_RParen.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_RParen.text.toString())
        }

        btn_Add.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_Add.text.toString())
        }

        btn_Subtract.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_Subtract.text.toString())
        }

        btn_Divide.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_Divide.text.toString())
        }

        btn_Multiply.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_Multiply.text.toString())
        }

        btn_Power.setOnClickListener{
            text_calc.setText(text_calc.text.toString() + btn_Power.text.toString())
        }


        btn_Clear.setOnClickListener {
            text_calc.text.clear()          //limpa a seção EditText
            text_info.text = ""         //limpa a seção TextView
        }


        btn_Equal.setOnClickListener {
            var result: Double = 0.0


            try {
                result = eval(text_calc.text.toString())
                text_info.text = result.toString()
            }catch (e : RuntimeException){
                val msg = "Equacao mal formatada"

                Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                text_info.text = ""
                //return@setOnClickListener
            }

            text_calc.text.clear()

        }

    }


    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
