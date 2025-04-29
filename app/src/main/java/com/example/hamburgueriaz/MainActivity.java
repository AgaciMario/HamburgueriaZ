package com.example.hamburgueriaz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity {

    // globals
    private int quantity = 1;

    // Views
    private TextView textViewQuantityDisplay;
    private TextView editTextTextClientName;
    private TextView textViewOrderResume;

    private CheckBox checkBoxBacon;
    private CheckBox checkBoxCheese;
    private CheckBox checkBoxOnionRings;

    private Button buttonRemoveItem;
    private Button buttonAddItem;
    private Button buttonSendOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Sets
        textViewQuantityDisplay = findViewById(R.id.textViewQuantityDisplay);
        editTextTextClientName = findViewById(R.id.editTextTextClientName);
        textViewOrderResume = findViewById(R.id.textViewOrderResume);

        checkBoxBacon = findViewById(R.id.checkBoxBacon);
        checkBoxCheese = findViewById(R.id.checkBoxCheese);
        checkBoxOnionRings = findViewById(R.id.checkBoxOnionRings);

        buttonRemoveItem = findViewById(R.id.buttonRemoveItem);
        buttonAddItem = findViewById(R.id.buttonAddItem);
        buttonSendOrder = findViewById(R.id.buttonSendOrder);

        // Events
        buttonRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subtrair();
                AtualizarViewQuantidade();
            }
        });

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Somar();
                AtualizarViewQuantidade();
            }
        });

        buttonSendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarPedido();
            }
        });
    }

    private void Somar(){
        int MAXBURGERQUANTITY = 50;
        if(quantity < MAXBURGERQUANTITY)
            quantity++;
    }

    private void Subtrair() {
        if(quantity > 1)
            quantity--;
    }

    private void AtualizarViewQuantidade(){
        textViewQuantityDisplay.setText(String.valueOf(quantity));
    }

    private void EnviarPedido(){
        String name = editTextTextClientName.getText().toString();

        if(!name.isEmpty()){
            boolean bacon = checkBoxBacon.isChecked();
            boolean cheese = checkBoxCheese.isChecked();
            boolean onionRings = checkBoxOnionRings.isChecked();

            float hamburgerPrice = 20.0f;
            if(bacon)
                hamburgerPrice += 2;
            if(cheese)
                hamburgerPrice += 2;
            if(onionRings)
                hamburgerPrice += 3;

            float finalOrderPrice = quantity * hamburgerPrice;

            CharSequence orderResume = MessageFormat.format(
                    "{0} \n" +
                            "Tem Bacon? {1} \n" +
                            "Tem Queijo? {2} \n" +
                            "Tem Onion Rings? {3} \n" +
                            "Quantidade: {4} \n" +
                            "Preço final: R$ {5} ", name, bacon ? "Sim" : "Não", cheese ? "Sim" : "Não", onionRings ? "Sim" : "Não", quantity, finalOrderPrice);

            textViewOrderResume.setText(orderResume);

            Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
            mailIntent.setData(Uri.parse("mailto:"));
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de ("+name+")");
            mailIntent.putExtra(Intent.EXTRA_TEXT, orderResume);
            startActivity(mailIntent);
        } else {
            Toast.makeText(MainActivity.this, "É necessário adicionar o nome do cliente", Toast.LENGTH_SHORT).show();
        }
    }
}