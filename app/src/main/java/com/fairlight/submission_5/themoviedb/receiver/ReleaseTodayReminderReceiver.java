package com.fairlight.submission_5.themoviedb.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.fairlight.submission_5.themoviedb.BuildConfig;
import com.fairlight.submission_5.themoviedb.MainActivity;
import com.fairlight.submission_5.themoviedb.R;
import com.fairlight.submission_5.themoviedb.model.Catalogue;
import com.fairlight.submission_5.themoviedb.ui.released_today.TodayMovieActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import androidx.core.app.NotificationCompat;
import cz.msebera.android.httpclient.Header;

public class ReleaseTodayReminderReceiver extends BroadcastReceiver {
    public static final int NO_MOVIE_ID = 0;
    public static final int SINGLE_MOVIE_ID = 1;
    public static final int MULTIPLE_MOVIE_ID = 100;
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    public String GROUP_KEY_RELEASED_TODAY_MOVIE = "group_key_released_today_movie";
    private String CHANNEL_ID = "Channel_2";
    private String CHANNEL_NAME = "Release Today Movie Reminder channel";
    private ArrayList<Catalogue> listTodayMovies = new ArrayList<>();
    private boolean isMovieEmpty = true;

    public static void setReleaseTodayReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseTodayReminderReceiver.class);
        Calendar calendar = Calendar.getInstance();
        //sometimes the notification doesn't show up in time, 1-3 minutes delay
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
        if (alarmManager != null) {
            // if notification time is before selected time, send notification the next day
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
            }
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(context, R.string.release_today_reminder_set, Toast.LENGTH_LONG).show();
    }

    public static void unsetReleaseTodayReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseTodayReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, R.string.release_today_reminder_unset, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String iso_code = Locale.getDefault().getLanguage();
        String today_date = getCurrentDate();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY
                + "&primary_release_date.gte=" + today_date
                + "&primary_release_date.lte=" + today_date;
        Log.d("Check url: ", url);

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Log.d("onSuccess: ", " Done");
                    String movie_result = new String(responseBody);
                    JSONObject movie_responseObject = new JSONObject(movie_result);
                    JSONArray movie_responseArray = movie_responseObject.getJSONArray("results");
                    if (movie_responseArray.length() > 0) {
                        for (int i = 0; i < movie_responseArray.length(); i++) {
                            Log.d("i= ", String.valueOf(i));
                            JSONObject movie = movie_responseArray.getJSONObject(i);
                            Catalogue movie_catalogue = new Catalogue();
                            //set action bar title : movie
                            if (iso_code.equals("in")) {
                                movie_catalogue.setApp_title("Film");
                            } else {
                                movie_catalogue.setApp_title("Movie");
                            }
                            //set movie catalogue id, id taken from api
                            movie_catalogue.setId_from_api(movie.getString("id"));
                            //set movie backdrop url
                            String movie_backdrop_filename = movie.getString("backdrop_path");
                            String movie_backdrop_url = "https://image.tmdb.org/t/p/w780" + movie_backdrop_filename;
                            movie_catalogue.setBackdrop_url(movie_backdrop_url);
                            //set movie title
                            movie_catalogue.setTitle(movie.getString("original_title"));
                            Log.d("Original Title:", movie_catalogue.getTitle());
                            //set movie release date
                            movie_catalogue.setReleased_date(format_date(movie.getString("release_date")));
                            //set movie user score
                            int movie_user_score = (int) ((movie.getDouble("vote_average")) * 10);
                            movie_catalogue.setUser_score(String.valueOf(movie_user_score).concat("%"));
                            //set movie poster url
                            String movie_poster_filename = movie.getString("poster_path");
                            String movie_poster_url = "https://image.tmdb.org/t/p/w154" + movie_poster_filename;
                            movie_catalogue.setPoster_url(movie_poster_url);
                            //set movie overview
                            movie_catalogue.setDescription(movie.getString("overview"));
                            //set movie language
                            String movie_language_code = movie.getString("original_language");
                            Locale movie_loc = new Locale(movie_language_code);
                            String movie_language = movie_loc.getDisplayLanguage(movie_loc);
                            movie_catalogue.setOriginal_language(movie_language);
                            //set movie popularity
                            movie_catalogue.setPopularity(movie.getString("popularity"));
                            //set movie vote count
                            movie_catalogue.setVote_count(movie.getString("vote_count"));
                            //add these data to arraylist
                            listTodayMovies.add(movie_catalogue);
                        }
                    } else {
                        listTodayMovies = null;
                    }
                } catch (Exception e) {
                    Log.e("Exception: ", "Fail to fetch data");
                }

                showNotification(context);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("onFailure: ", "url maybe wrong");
            }
        });
    }

    private void showNotification(Context context) {
        if (listTodayMovies != null && listTodayMovies.size() > 0) {
            isMovieEmpty = false;
        }

        if (isMovieEmpty) {
            if (listTodayMovies != null) {
                Log.d("isMovieEmpty?", String.valueOf(listTodayMovies.size()));
            }
            noReleasedMovie(context);
        } else {
            if (Objects.requireNonNull(listTodayMovies).size() == 1) {
                singleNotification(context, listTodayMovies.get(0));
            } else { //if released movie today is more than 1, only show 2 notifications
                multipleNotification(context, listTodayMovies);
            }
        }
    }

    private void noReleasedMovie(Context context) {

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_red_24dp)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setContentTitle(context.getString(R.string.release_reminder_title))
                .setContentText(context.getString(R.string.release_today_reminder_no_new_movies_notif))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(NO_MOVIE_ID, notification);
        }
    }

    private void singleNotification(Context context, Catalogue catalogue) {
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, TodayMovieActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String movieTitle = catalogue.getTitle().concat(" ");
        String contentText = movieTitle.concat(context.getString(R.string.release_today_reminder_message));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_red_24dp)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setContentTitle(context.getString(R.string.release_reminder_title))
                .setContentText(contentText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(SINGLE_MOVIE_ID, notification);
        }
    }

    private void multipleNotification(Context context, ArrayList<Catalogue> catalogues) {
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, TodayMovieActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                .setBigContentTitle(catalogues.size() + " " + context.getString(R.string.release_today_inbox_style_big_title_notif));

        for (int i = 0; i < catalogues.size(); i++) {
            inboxStyle.addLine(catalogues.get(i).getTitle().concat(" ").concat(context.getString(R.string.release_today_content_text_multiple_notif)));
        }

        NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_red_24dp)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setContentTitle(context.getString(R.string.release_reminder_title))
                .setContentText(catalogues.size() + " " + context.getString(R.string.release_today_content_text_notif))
                .setStyle(inboxStyle)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setGroup(GROUP_KEY_RELEASED_TODAY_MOVIE)
                .setAutoCancel(true)
                .setGroupSummary(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            summaryBuilder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification summaryNotification = summaryBuilder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(MULTIPLE_MOVIE_ID, summaryNotification);
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    private String format_date(String raw_date) {
        String formatted_date = "";
        if (!raw_date.equals("")) {
            String locale_language = Locale.getDefault().getLanguage();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = null;
            try {
                date = sdf.parse(raw_date);
            } catch (ParseException e) {
                Log.e("Parse Error", "try again");
            }
            if (locale_language.equals("en")) {
                sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            } else if (locale_language.equals("in")) {
                sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            }
            if (date != null) {
                formatted_date = sdf.format(date);
            }
        }
        return formatted_date;
    }
}
