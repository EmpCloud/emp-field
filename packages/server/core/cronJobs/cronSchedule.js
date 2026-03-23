import { CronJob } from 'cron';
import axios from 'axios';
import Currency from './../reports/report.currency.js';

const fetchExchangeRates = async () => {
  try {
    console.log('called');
    const response = await axios.get(
      'https://v6.exchangerate-api.com/v6/7f3f02c3e6f0099043c03c06/latest/INR'
    );

    if (response.data.result === 'success') {
      const {
        result,
        documentation,
        terms_of_use,
        time_last_update_unix,
        time_last_update_utc,
        time_next_update_unix,
        time_next_update_utc,
        base_code,
        conversion_rates,
      } = response.data;

      const dataExist = await Currency.findOne({ base_code });

      if (dataExist) {
        await Currency.findOneAndUpdate(
          { base_code },
          {
            result,
            documentation,
            terms_of_use,
            time_last_update_unix,
            time_last_update_utc,
            time_next_update_unix,
            time_next_update_utc,
            base_code,
            conversion_rates,
            currencyDetails: conversion_rates,
          },
          { new: true } 
        );
      } else {
        await Currency.findOneAndUpdate(
          {},
          {
            result,
            documentation,
            terms_of_use,
            time_last_update_unix,
            time_last_update_utc,
            time_next_update_unix,
            time_next_update_utc,
            base_code,
            conversion_rates,
            currencyDetails: conversion_rates,
          },
          { upsert: true, new: true } 
        );
      }

    } else {
      console.error('Failed to fetch exchange rates');
    }
  } catch (error) {
    console.error('Error fetching exchange rates:', error);
  }
};

export const currency = new CronJob(
  '0 12 * * *',  // Runs at 12:00 PM every day
  fetchExchangeRates,
  null,
  false,
  'Asia/Kolkata'
);
// setTimeout(() => {
//   if (currency.running) {
//     console.log('Cron job is running.');
//   } else {
//     console.log('Cron job is not running.');
//   }
// }, 30000);

