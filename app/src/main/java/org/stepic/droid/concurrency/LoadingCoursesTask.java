package org.stepic.droid.concurrency;

import org.stepic.droid.base.MainApplication;
import org.stepic.droid.core.IShell;
import org.stepic.droid.model.Course;
import org.stepic.droid.store.operations.DbOperationsCourses;
import org.stepic.droid.web.CoursesStepicResponse;
import org.stepic.droid.web.IApi;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

public class LoadingCoursesTask extends StepicTask<Void, Void, CoursesStepicResponse> {

    @Inject
    IShell mShell;

    private DbOperationsCourses.Table mCourseType;
    private int mPage;


    public LoadingCoursesTask(DbOperationsCourses.Table courseType, int page) {
        super(MainApplication.getAppContext());
        mPage = page;
        MainApplication.component(mContext).inject(this);

        mCourseType = courseType;
    }

    @Override
    protected CoursesStepicResponse doInBackgroundBody(Void... params) {
        IApi api = mShell.getApi();
        List<Course> courseList = null;
        CoursesStepicResponse stepicResponse = null;
        CoursesStepicResponse resultStepicResponse;
        try {
            switch (mCourseType) {
                case enrolled:
                    stepicResponse = api.getEnrolledCourses(mPage);
                    break;
                case featured:
                    stepicResponse = api.getFeaturedCourses(mPage);
                    break;
            }

            courseList = stepicResponse.getCourses();
        } finally {
            if (courseList != null) {
//                List<Course> cachedCourses = getCachedCourses();

                DbOperationsCourses dbOperationCourses = mShell.getDbOperationsCourses(mCourseType);

                try {
                    dbOperationCourses.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                    //todo: if db is not exist app will crash.
                }
                try {

                    if (mPage == 1) {
                        dbOperationCourses.clearCache();
                    }

//                    for (Course courseItem : cachedCourses) {
//                        if (!courseList.contains(courseItem)) {
//                            dbOperationCourses.deleteCourse(courseItem);//remove outdated courses from cache
//                            courseList.remove(courseItem);
//                        }
//                    }

                    for (Course newCourse : courseList) {
                        if (!dbOperationCourses.isCourseInDB(newCourse)) {
                            dbOperationCourses.addCourse(newCourse);//add new to persistent cache
                        }
                    }
                } finally {
                    dbOperationCourses.close();
                }
                //all courses are cached now
            }

            courseList = getCachedCourses(); //get from cache;
            resultStepicResponse = new CoursesStepicResponse(courseList, stepicResponse.getMeta());

        }
        return resultStepicResponse;

    }

    private List<Course> getCachedCourses() {
        //todo: change to filter method with void getCachecCourses(List<Course> listForFilter)
        DbOperationsCourses dbOperationCourses = mShell.getDbOperationsCourses(mCourseType);

        try {
            dbOperationCourses.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Course> cachedCourses = dbOperationCourses.getAllCourses();
        dbOperationCourses.close();

        return cachedCourses;
    }
}
