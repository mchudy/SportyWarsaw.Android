package pl.sportywarsaw.infrastructure;

import javax.inject.Singleton;

import dagger.Component;
import pl.sportywarsaw.activities.AddMeetingActivity;
import pl.sportywarsaw.activities.LoginActivity;
import pl.sportywarsaw.activities.MainActivity;
import pl.sportywarsaw.activities.MeetingDetailsActivity;
import pl.sportywarsaw.activities.RegisterActivity;
import pl.sportywarsaw.activities.SearchFriendsActivity;
import pl.sportywarsaw.activities.SportsFacilityDetailsActivity;
import pl.sportywarsaw.activities.UserProfileActivity;
import pl.sportywarsaw.fragments.FriendsFragment;
import pl.sportywarsaw.fragments.MeetingCommentsFragment;
import pl.sportywarsaw.fragments.MeetingDetailsFragment;
import pl.sportywarsaw.fragments.MeetingParticipantsFragment;
import pl.sportywarsaw.fragments.MeetingsTabFragment;
import pl.sportywarsaw.fragments.SportsFacilitiesFragment;

/**
 * Component injecting dependencies from {@link RestServicesModule}
 * and {@link ApplicationModule}
 *
 * @author Marcin Chudy
 */
@Singleton
@Component(modules = {RestServicesModule.class, ApplicationModule.class})
public interface ApplicationComponent {
    void inject(MainActivity activity);
    void inject(SearchFriendsActivity activity);
    void inject(LoginActivity activity);
    void inject(RegisterActivity activity);
    void inject(SportsFacilitiesFragment startupFragment);
    void inject(MeetingsTabFragment meetingsFragment);
    void inject(SportsFacilityDetailsActivity sportsFacilityDetailsActivity);
    void inject(FriendsFragment friendsFragment);
    void inject(AddMeetingActivity addMeetingActivity);
    void inject(MeetingDetailsActivity meetingDetailsActivity);
    void inject(MeetingParticipantsFragment meetingParticipantsFragment);
    void inject(MeetingDetailsFragment meetingDetailsFragment);
    void inject(UserProfileActivity userProfileActivity);
    void inject(MeetingCommentsFragment meetingCommentsFragment);
}
