//Created by Jordan Lagache, Jonathan Noel

#include <string.h>
//#fuses HS, NOPROTECT, NOLVP, NOWDT
#use rs232(baud=9600,xmit=PIN_C6,rcv=PIN_C7,bits=8)
#use delay(clock=4000000) //Freq du PIC: 4MHz
/*
#BYTE PORT_B_PORT=0x81
#BIT PORT_B_BIT0=PORT_B_PORT.0
#BIT PORT_B_BIT1=PORT_B_PORT.1 
*/
void alert(int temp, int limit);
void get_temp();
int toDCB(int val);
int temp;
void main()
{
   setup_adc_ports(AN0); //AN0
   setup_adc(ADC_CLOCK_DIV_32);
   set_adc_channel(0);
   enable_interrupts(INT_RDA); //INT_RDA --> PROVOQUE ERREUR
   enable_interrupts(GLOBAL);

   int limit = 35;
   int dcb_temp;
   while(TRUE)
   {
      delay_ms(500);
      int dcb_temp = toDCB((read_adc()*100/1024));
      int temp = (read_adc()*100/1024);
      set_adc_channel(0);
      output_d(dcb_temp); //dcb_temp
      printf("%d",temp);
      alert(temp,limit);
      //get_temp();
   }
}
//Permet d'allumer une led en fonction de l'état dans laquelle on se trouve.
void alert(int temp, int limit){
   if(temp >= limit){ //Etat d'alerte, température trop élevé
         output_bit(PIN_B1, 1);
         output_bit(PIN_B0, 0);
      }
      else { //Etat normal, pas de problème.
         output_bit(PIN_B0, 1);
         output_bit(PIN_B1, 0);
      }
}
int toDCB(int val){
   return (((val / 10) % 10) <<4) + (val % 10);
}
void get_temp(){
   char * pEnd;
   char src, value, bufferChar[4];
   int j = 0;
   value = getc();
   if(value == ';'){
      if(j==1){strcpy(src,"0"+bufferChar);}
      else{strcpy(src, bufferChar);}
      temp = strtol(bufferChar,&pEnd,10);
      j = 0;
      //Clear buff
      memset(&bufferChar[0], 0, sizeof(bufferChar));
      printf("%c",bufferChar);
   
   }else{
      bufferChar[j] = value;
      j++;
   }
}
