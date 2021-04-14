#type vertex
#version 330 core
layout (location=0) in vec3 aPos; // si mette a prima del nome della variabile per definire gli shaders
layout (location=1) in vec4 aColor;

out vec4 fColor;
void main()
{
    fColor = aColor; // gli sto passando il colore dello shaders in cui mi trovo
    gl_Position = vec4(aPos, 1.0); // è una variabile che serve per riempire il vec4 dove aPos è l'inizio del vettore e 1.0 è la fine
}

#type fragment
#version 330 core

in vec4 fColor ;

out vec4 color ;

void main()
{
    color = fColor;
}
