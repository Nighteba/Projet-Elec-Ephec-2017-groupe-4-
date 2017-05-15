#include <main.h>

#include <stdlib.h>
#include <string.h>
// No watchdog, no protect, parameter for serial_isr
#fuses HS, NOPROTECT, NOLVP, NOWDT
//PIC CLOCK: 20MHz, MUST BE the same for CRYSTAL clock
#use delay(clock=20000000)
//Parameter for rs232, synchronized with physical parameters
#use rs232(baud=9600,xmit=PIN_C6,rcv=PIN_C7,bits=8)
void alert(int temp, int limit);
int toDCB(int val);
int temp; // temperature value
int limit=35; //limit default temperature value 
char  buffer[4]; //used for serial_isr
int j = 0; //used for serial_isr
/* INTERRUPTION RECEIVE DATA AVAILABLE (RDA) BLOCK*/
#INT_RDA
/*
This function has been created by the group of Stefan Gotowski, we re-used it, they agreed.
This function is called when an interruption is sent to TX PIN with DATA AVAILABLE.
*/

//code inspiré de Jordan Lagache et Jonathan Noel
void serial_isr(void){
   char src;
   char * pEnd;
   char value;
   value = getc();
   if(value == ';'){
      if(j==1){strcpy(src,"0"+buffer);}
      else{strcpy(src, buffer);}
      limit = strtol(buffer,&pEnd,10);
      j = 0;
      memset(&buffer[0], 0, sizeof(buffer));
   }else{
      buffer[j] = value;
      j++;
   }
}
void main()
{
   //ANALOGIC PORT && settings
   setup_adc_ports(AN0); 
   setup_adc(ADC_CLOCK_DIV_32);
   set_adc_channel(0);
   //INTERRUPTIONS declarations
   ENABLE_INTERRUPTS(INT_RDA);
   ENABLE_INTERRUPTS(GLOBAL);
   int dcb_temp; //converted temperature value
   while(TRUE)
   {
      delay_ms(500);
      int dcb_temp = toDCB((read_adc()*100/1024));
      int temp = (read_adc()*100/1024);
      set_adc_channel(0);
      output_d(dcb_temp); //Display on 7seg displayers
      printf("%d\n",temp);//display temperature
      alert(temp,limit); 
      delay_ms(500);
   }
}
//Permet d'allumer une led en fonction de l'état dans laquelle on se trouve.
/*
Is used in order to light an alert LED if the temperature is too high and
to light a OK LED if the temperature is under the limit.
B1 --> Alert LED on PIN_B1
B0 --> OK LED on PIN_B0
*/
void alert(int temp, int limit){ 
   if(temp >= limit){ //Alert state
         output_bit(PIN_B1, 1); //Send a high voltage on PIN_B1
         output_bit(PIN_B0, 0); //low voltage on B0
      }
      else { //Etat normal, pas de problème.
         output_bit(PIN_B0, 1); //High voltage on B0
         output_bit(PIN_B1, 0);//Low voltage on B1
      }
}
/*
This function convert binary value to DCB in order to send them to PIN_Dx ports.
*/
int toDCB(int val){
   return (((val / 10) % 10) <<4) + (val % 10);
}

