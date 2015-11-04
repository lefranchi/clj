#!/bin/sh

pkill -f Transmissor

echo "Transmissor Finalizado."

sleep 2

pkill -f Receptor

echo "Receptor Finalizado."
